// Rasterises SVG files with Playwright's Chromium.
// Usage: node render.mjs jobs.json, where jobs.json is [{ "svg": path, "png": path, "size": px }].
import { readFileSync } from "node:fs";
import { createRequire } from "node:module";
import { execSync } from "node:child_process";

const require = createRequire(import.meta.url);
const { chromium } = require(`${execSync("npm root -g").toString().trim()}/playwright`);

const jobs = JSON.parse(readFileSync(process.argv[2], "utf8"));
const browser = await chromium.launch();
const page = await browser.newPage();
for (const { svg, png, size } of jobs) {
  await page.setViewportSize({ width: size, height: size });
  const markup = readFileSync(svg, "utf8").replace("<svg ", `<svg width="${size}" height="${size}" `);
  await page.setContent(`<html><body style="margin:0;background:transparent">${markup}</body></html>`);
  await page.screenshot({ path: png, omitBackground: true, clip: { x: 0, y: 0, width: size, height: size } });
}
await browser.close();
