// Startup-performance measurement for the web smoke job: cold and repeat
// load timing (navigation start -> `#loader` gaining `.hidden`, the same
// signal Main.kt's hideLoader() sets and web-smoke.mjs already waits on)
// under different throttle profiles, plus bytes/requests for the cold load.
// Kept separate from web-smoke.mjs so that file stays focused on functional
// checks. Profile values (Fast 4G: 9 Mbit/s / 60 ms / CPU x4) and the 10 s
// soft threshold are from Issue #69's baseline.

const FAST_4G_NETWORK_CONDITIONS = {
  offline: false,
  latency: 60, // ms
  downloadThroughput: (9_000_000) / 8, // 9 Mbit/s -> bytes/s
  uploadThroughput: (9_000_000) / 8,
};
const CPU_THROTTLE_RATE = 4;
export const WARN_THRESHOLD_MS = 10_000;

// Runs in-page: records the ms from navigation start to `#loader` gaining
// `.hidden`, via performance.now() so the number isn't skewed by Node/IPC
// round trips. Installed with addInitScript, so it re-arms on every
// navigation in the context (needed for the warm-cache repeat load).
function installLoaderTimingHook() {
  window.__smokeLoaderHiddenAtMs = null;
  const check = () => {
    const loader = document.getElementById('loader');
    if (loader && loader.classList.contains('hidden')) {
      window.__smokeLoaderHiddenAtMs = performance.now();
      return true;
    }
    return false;
  };
  if (check()) return;
  const observer = new MutationObserver(() => {
    if (check()) observer.disconnect();
  });
  const attach = () => {
    if (document.documentElement) {
      observer.observe(document.documentElement, {
        attributes: true,
        subtree: true,
        attributeFilter: ['class'],
      });
      if (check()) observer.disconnect();
    } else {
      requestAnimationFrame(attach);
    }
  };
  attach();
}

async function waitForLoaderHiddenMs(page, timeoutMs) {
  await page.waitForFunction(() => window.__smokeLoaderHiddenAtMs !== null, null, { timeout: timeoutMs });
  return page.evaluate(() => window.__smokeLoaderHiddenAtMs);
}

function attachErrorCapture(page, errors, label, isKnownBenign) {
  page.on('pageerror', (err) => errors.push(`perf ${label}: pageerror: ${err.message}`));
  page.on('console', (msg) => {
    if (msg.type() === 'error' && !isKnownBenign(msg.text())) {
      errors.push(`perf ${label}: console.error: ${msg.text()}`);
    }
  });
}

// Sums encodedDataLength (bytes actually on the wire, i.e. after gzip) and
// counts requests for one navigation via the CDP Network domain.
function trackTransfer(client) {
  let requestCount = 0;
  let bytes = 0;
  const seen = new Set();
  client.on('Network.requestWillBeSent', () => {
    requestCount += 1;
  });
  client.on('Network.loadingFinished', (event) => {
    if (seen.has(event.requestId)) return;
    seen.add(event.requestId);
    bytes += event.encodedDataLength ?? 0;
  });
  return () => ({ requestCount, bytes });
}

// Cold load in a fresh context (no HTTP cache), optionally throttled.
// Caller closes the returned context once done with it (or, for the
// Fast 4G profile, reuses `page` first for the warm-cache repeat load).
async function measureCold(browser, url, viewport, errors, options) {
  const { label, networkConditions, cpuThrottleRate, timeoutMs, captureTransfer, isKnownBenign } = options;
  const context = await browser.newContext({ viewport, locale: 'en-US' });
  await context.addInitScript(installLoaderTimingHook);
  const page = await context.newPage();
  attachErrorCapture(page, errors, label, isKnownBenign);

  const client = await context.newCDPSession(page);
  await client.send('Network.enable');
  const getTransfer = captureTransfer ? trackTransfer(client) : null;
  if (networkConditions) await client.send('Network.emulateNetworkConditions', networkConditions);
  if (cpuThrottleRate) await client.send('Emulation.setCPUThrottlingRate', { rate: cpuThrottleRate });

  await page.goto(url, { waitUntil: 'commit' });
  const ms = await waitForLoaderHiddenMs(page, timeoutMs);

  return { ms, transfer: getTransfer ? getTransfer() : null, context, page };
}

// Repeat load: navigates again on the same context/page (warm HTTP cache),
// under whatever throttle profile that context's CDP session already has
// (Network/CPU emulation is per-target and survives a same-tab navigation).
async function measureRepeat(page, url, timeoutMs) {
  await page.goto(url, { waitUntil: 'commit' });
  return waitForLoaderHiddenMs(page, timeoutMs);
}

function formatSeconds(ms) {
  return `${(ms / 1000).toFixed(2)} s`;
}

function formatBytes(bytes) {
  return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
}

// Runs cold (no throttling), cold (Fast 4G/CPUx4) and repeat (Fast 4G/CPUx4,
// warm HTTP cache) loads, plus the cold-load transfer size, and returns
// step-summary lines. Pushes any page/console errors it sees onto `errors`
// (same as the functional checks), but a slow number only logs a
// `::warning::` annotation -- it never fails the job.
export async function measureWebPerf(browser, baseUrl, viewport, errors, isKnownBenign) {
  const coldNone = await measureCold(browser, baseUrl, viewport, errors, {
    label: 'cold(none)',
    networkConditions: null,
    cpuThrottleRate: null,
    timeoutMs: 30_000,
    captureTransfer: true,
    isKnownBenign,
  });
  await coldNone.context.close();

  const coldFast4g = await measureCold(browser, baseUrl, viewport, errors, {
    label: 'cold(Fast 4G/CPUx4)',
    networkConditions: FAST_4G_NETWORK_CONDITIONS,
    cpuThrottleRate: CPU_THROTTLE_RATE,
    timeoutMs: 60_000,
    captureTransfer: false,
    isKnownBenign,
  });
  const repeatFast4gMs = await measureRepeat(coldFast4g.page, baseUrl, 60_000);
  await coldFast4g.context.close();

  if (coldFast4g.ms > WARN_THRESHOLD_MS) {
    console.log(
      `::warning::Web startup is slow: cold load on Fast 4G/CPUx4 took ${formatSeconds(coldFast4g.ms)} (> ${formatSeconds(WARN_THRESHOLD_MS)})`,
    );
  }

  const lines = [
    '## Startup performance',
    '',
    '| Profile | Time to `#loader.hidden` |',
    '|---|---|',
    `| Cold, no throttling | ${formatSeconds(coldNone.ms)} |`,
    `| Cold, Fast 4G / CPU×4 | ${formatSeconds(coldFast4g.ms)} |`,
    `| Repeat (warm HTTP cache), Fast 4G / CPU×4 | ${formatSeconds(repeatFast4gMs)} |`,
    '',
    `Bytes on the wire (cold, no throttling): ${formatBytes(coldNone.transfer.bytes)}, ${coldNone.transfer.requestCount} requests.`,
  ];

  return { lines };
}
