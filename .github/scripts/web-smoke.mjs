#!/usr/bin/env node
// CI smoke test for the wasm web build. Loads the served bundle in Chromium,
// taps through the 5 bottom-bar tabs by coordinate, and fails on any page or
// console error. Playwright is installed by the caller outside the repo tree
// (see .github/workflows/ci.yml); NODE_PATH points this script at it since a
// bare `import 'playwright'` ignores NODE_PATH but `require()` honours it.
import { createRequire } from 'node:module';
import { mkdir, appendFile } from 'node:fs/promises';
import path from 'node:path';

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

  await browser.close();

  const pass = errors.length === 0;
  const summary = [
    '# Web smoke test',
    '',
    pass ? '**Result: PASS**' : '**Result: FAIL**',
    '',
    '## Tabs captured',
    ...shots.map((s) => `- ${s.name}: \`${s.file}\``),
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
