#!/usr/bin/env node
// CI smoke test for the wasm web build. Loads the served bundle in Chromium,
// taps through the 5 bottom-bar tabs by coordinate, opens the paywall from a
// locked Home card and closes it with browser back (must stay on the site),
// opens it again and closes it with X (must not add a history entry, so browser
// back afterwards doesn't reopen it),
// forces a WebGL context loss (the app must reload itself and come back), and
// fails on any page or console error. Playwright is installed by the caller outside the repo tree
// (see .github/workflows/ci.yml); NODE_PATH points this script at it since a
// bare `import 'playwright'` ignores NODE_PATH but `require()` honours it.
import { createRequire } from 'node:module';
import { mkdir, appendFile } from 'node:fs/promises';
import path from 'node:path';
import { measureWebPerf } from './web-perf.mjs';

const require = createRequire(import.meta.url);
const { chromium } = require('playwright');

const BASE_URL = process.env.SMOKE_BASE_URL ?? 'http://localhost:8000';
const OUT_DIR = process.env.SMOKE_OUT_DIR ?? 'web-smoke-screenshots';
const VIEWPORT = { width: 402, height: 874 };
// Bottom bar is at y ~= 840; x = centre of each fifth of the 402-wide viewport.
const TAB_Y = 840;
const TABS = [
  { name: 'tab-1', x: 40 },
  { name: 'tab-2', x: 121 },
  { name: 'tab-3', x: 201 },
  { name: 'tab-4', x: 281 },
  { name: 'tab-5', x: 362 },
];

// Home, scrolled down by HOME_SCROLL px from the top: the first category card
// (Career, locked) is at LOCKED_CARD.
const HOME_SCROLL = 400;
const LOCKED_CARD = { x: 88, y: 420 };
// Paywall's X button, top left.
const PAYWALL_CLOSE = { x: 31, y: 28 };

// Kotlin/Wasm-Skiko runtime notice, logged on every startup regardless of app
// code (tracked upstream at https://kotl.in/vr3szr). Not a regression to catch.
const KNOWN_BENIGN_CONSOLE_ERRORS = [/Accessing `memory` via `wasmExports` is deprecated/];

function isKnownBenign(text) {
  return KNOWN_BENIGN_CONSOLE_ERRORS.some((re) => re.test(text));
}

async function waitForCanvasReady(page) {
  try {
    await page.waitForFunction(
      () => {
        const canvas = document.querySelector('canvas');
        return !!canvas && canvas.width > 0 && canvas.height > 0;
      },
      { timeout: 15000 },
    );
    // Compose keeps painting for a moment after the canvas first appears.
    await page.waitForTimeout(1000);
  } catch {
    // Readiness check itself timed out: fall back to a fixed wait.
    await page.waitForTimeout(6000);
  }
}

// Finds the Compose canvas under #composeTarget (possibly in a shadow root),
// drops its WebGL context and restores it shortly after, as Chrome on Android
// does to background tabs. Returns false if the canvas or extension is missing.
function loseWebGlContext() {
  const find = (root) => {
    const canvas = root.querySelector('canvas');
    if (canvas) return canvas;
    for (const el of root.querySelectorAll('*')) {
      const found = el.shadowRoot && find(el.shadowRoot);
      if (found) return found;
    }
    return null;
  };
  const canvas = find(document.getElementById('composeTarget'));
  const gl = canvas && (canvas.getContext('webgl2') || canvas.getContext('webgl'));
  const ext = gl && gl.getExtension('WEBGL_lose_context');
  if (!ext) return false;
  ext.loseContext();
  setTimeout(() => ext.restoreContext(), 100);
  return true;
}

async function checkContextLossReload(page, errors) {
  const reloaded = page.waitForEvent('load', { timeout: 15000 }).then(() => true, () => false);
  if (!(await page.evaluate(loseWebGlContext))) {
    errors.push('context loss: no Compose canvas with WEBGL_lose_context');
    return;
  }
  if (!(await reloaded)) {
    errors.push('context loss: the page did not reload');
    return;
  }
  try {
    await page.waitForFunction(() => document.getElementById('loader')?.classList.contains('hidden'), null, {
      timeout: 30000,
    });
    await page.waitForTimeout(1000);
  } catch {
    errors.push('context loss: the app did not come back after the reload (loader still shown)');
  }
}

// Home tab -> locked card -> paywall (URL gets #paywall) -> browser back -> Home.
async function checkPaywallBack(page, errors, shots) {
  const shot = async (name) => {
    const file = path.join(OUT_DIR, `${name}.png`);
    await page.screenshot({ path: file });
    shots.push({ name, file });
  };
  await page.mouse.click(TABS[0].x, TAB_Y); // switching tabs resets Home to the top
  await page.waitForTimeout(1000);
  await page.mouse.move(VIEWPORT.width / 2, VIEWPORT.height / 2);
  await page.mouse.wheel(0, HOME_SCROLL);
  await page.waitForTimeout(1000);
  await page.mouse.click(LOCKED_CARD.x, LOCKED_CARD.y);
  await page.waitForTimeout(1500);
  await shot('06-paywall');
  if (!page.url().includes('#paywall')) {
    errors.push(`paywall: URL has no #paywall after tapping a locked card (${page.url()})`);
    return;
  }
  await page.goBack();
  await page.waitForTimeout(1500);
  await shot('07-back-on-home');
  const url = page.url();
  if (!url.startsWith(BASE_URL) || url.includes('#paywall')) {
    errors.push(`paywall: browser back did not return to Home on the site (${url})`);
  }
}

// Paywall -> X must go through history.back(): the URL returns to #main, history
// doesn't grow, and browser back afterwards must not reopen the paywall.
async function checkPaywallCloseX(page, errors, shots) {
  const shot = async (name) => {
    const file = path.join(OUT_DIR, `${name}.png`);
    await page.screenshot({ path: file });
    shots.push({ name, file });
  };
  await page.mouse.click(LOCKED_CARD.x, LOCKED_CARD.y); // Home kept its scroll after the back above
  await page.waitForTimeout(1500);
  if (!page.url().includes('#paywall')) {
    errors.push(`paywall X: URL has no #paywall after tapping a locked card (${page.url()})`);
    return;
  }
  const historyOpen = await page.evaluate(() => history.length);
  await page.mouse.click(PAYWALL_CLOSE.x, PAYWALL_CLOSE.y);
  await page.waitForTimeout(1500);
  await shot('08-closed-with-x');
  const afterX = page.url();
  const historyAfter = await page.evaluate(() => history.length);
  if (!afterX.includes('#main')) errors.push(`paywall X: URL is not #main after X (${afterX})`);
  if (historyAfter > historyOpen) {
    errors.push(`paywall X: X added a history entry (${historyOpen} -> ${historyAfter})`);
  }
  await page.goBack();
  await page.waitForTimeout(1500);
  if (page.url().includes('#paywall')) errors.push('paywall X: browser back after X reopened the paywall');
  if (!page.url().startsWith(BASE_URL)) {
    // Left the site, as history had it: come back for the context-loss check.
    await page.goForward();
    await waitForCanvasReady(page);
  }
  await shot('09-back-after-x');
}

async function main() {
  await mkdir(OUT_DIR, { recursive: true });

  const errors = [];
  const browser = await chromium.launch();
  const context = await browser.newContext({ viewport: VIEWPORT, locale: 'en-US' });
  const page = await context.newPage();

  page.on('pageerror', (err) => errors.push(`pageerror: ${err.message}`));
  page.on('console', (msg) => {
    if (msg.type() === 'error' && !isKnownBenign(msg.text())) errors.push(`console.error: ${msg.text()}`);
  });

  const shots = [];

  await page.goto(BASE_URL, { waitUntil: 'load' });
  await waitForCanvasReady(page);
  const startShot = path.join(OUT_DIR, '00-start.png');
  await page.screenshot({ path: startShot });
  shots.push({ name: 'start', file: startShot });

  for (const [i, tab] of TABS.entries()) {
    await page.mouse.click(tab.x, TAB_Y);
    await page.waitForTimeout(1000);
    const file = path.join(OUT_DIR, `${String(i + 1).padStart(2, '0')}-${tab.name}.png`);
    await page.screenshot({ path: file });
    shots.push({ name: tab.name, file });
  }

  await checkPaywallBack(page, errors, shots);
  await checkPaywallCloseX(page, errors, shots);

  await checkContextLossReload(page, errors);
  const reloadShot = path.join(OUT_DIR, '10-after-context-loss.png');
  await page.screenshot({ path: reloadShot });
  shots.push({ name: 'after-context-loss', file: reloadShot });

  const perf = await measureWebPerf(browser, BASE_URL, VIEWPORT, errors, isKnownBenign);

  await browser.close();

  const pass = errors.length === 0;
  const summary = [
    '# Web smoke test',
    '',
    pass ? '**Result: PASS**' : '**Result: FAIL**',
    '',
    '## Screens captured',
    ...shots.map((s) => `- ${s.name}: \`${s.file}\``),
    '',
    ...perf.lines,
  ];
  if (!pass) {
    summary.push('', '## Errors', ...errors.map((e) => `- ${e}`));
  }
  console.log(summary.join('\n'));

  if (process.env.GITHUB_STEP_SUMMARY) {
    await appendFile(process.env.GITHUB_STEP_SUMMARY, `${summary.join('\n')}\n`);
  }

  if (!pass) {
    console.error(`Web smoke test failed with ${errors.length} error(s).`);
    process.exitCode = 1;
  }
}

main().catch((err) => {
  console.error(err);
  process.exitCode = 1;
});
