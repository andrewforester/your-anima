// Service worker of the production web build (#71): one cache per build, cache-first for the app's files, so a
// repeat visit starts without waiting for GitHub Pages' `max-age=600` revalidations. The wasmJsBrowserDistribution
// post-step in composeApp/build.gradle.kts replaces the two placeholders below; every deploy therefore produces a
// byte-different sw.js, which the browser picks up on its next navigation (index.html registers it with
// `updateViaCache: 'none'`). The dev server serves this file unfilled, and nothing registers it there.
// Lifecycle and the update flow are described in composeApp/src/wasmJsMain/kotlin/app/youranima/agents.md.
const BUILD_ID = '949354c08ec76e39';
// Paths relative to the scope: index.html, composeApp.js, both wasm, styles/icons and the first-frame resources.
const PRECACHE = ['apple-touch-icon.png', 'bfa5198fb2fe683c613a.wasm', 'composeApp.js', 'd12069982c3e1eeddf99.wasm', 'favicon-32.png', 'favicon.svg', 'index.html', 'styles.css', 'composeResources/app.youranima.resources/drawable/home_avatar_character.jpg', 'composeResources/app.youranima.resources/drawable/home_avatar_thumb.jpg', 'composeResources/app.youranima.resources/drawable/home_hero_background.xml', 'composeResources/app.youranima.resources/drawable/home_ic_book.xml', 'composeResources/app.youranima.resources/drawable/home_ic_category_health.xml', 'composeResources/app.youranima.resources/drawable/home_ic_category_love.xml', 'composeResources/app.youranima.resources/drawable/home_ic_check.xml', 'composeResources/app.youranima.resources/drawable/home_ic_cross.xml', 'composeResources/app.youranima.resources/drawable/home_ic_crystal_ball.xml', 'composeResources/app.youranima.resources/drawable/home_ic_info.xml', 'composeResources/app.youranima.resources/drawable/home_ic_lock.xml', 'composeResources/app.youranima.resources/drawable/home_ic_moon.xml', 'composeResources/app.youranima.resources/drawable/home_ic_plus.xml', 'composeResources/app.youranima.resources/drawable/home_ic_settings.xml', 'composeResources/app.youranima.resources/drawable/home_ic_star.xml', 'composeResources/app.youranima.resources/drawable/home_ic_sun.xml', 'composeResources/app.youranima.resources/drawable/home_tip_stars.xml', 'composeResources/app.youranima.resources/drawable/ic_briefcase.xml', 'composeResources/app.youranima.resources/drawable/ic_heart.xml', 'composeResources/app.youranima.resources/drawable/ic_message_circle.xml', 'composeResources/app.youranima.resources/drawable/ic_user.xml', 'composeResources/app.youranima.resources/font/geist_bold.ttf', 'composeResources/app.youranima.resources/font/geist_medium.ttf', 'composeResources/app.youranima.resources/font/geist_regular.ttf', 'composeResources/app.youranima.resources/font/geist_semibold.ttf', 'composeResources/app.youranima.resources/values/strings.commonMain.cvr', 'composeResources/app.youranima.resources/values/strings_home.commonMain.cvr'];

const SCOPE = self.registration.scope;
// Scoped cache names: the prod app and branch previews (a sub-path of it) share one origin, hence one Cache Storage.
const CACHE_PREFIX = `your-anima@${SCOPE}@`;
const CACHE = CACHE_PREFIX + BUILD_ID;
const PRECACHED = new Set(PRECACHE);
const MATCH = { ignoreVary: true };

self.addEventListener('install', (event) => {
  // `no-cache`: revalidate against the server (304s after a cold load) so an HTTP-cached file of the previous build
  // can't slip into this build's cache; any failure (e.g. a half-propagated deploy) fails the install.
  event.waitUntil(
    caches.open(CACHE).then((cache) => cache.addAll(PRECACHE.map((path) => new Request(path, { cache: 'no-cache' })))),
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((key) => key.startsWith(CACHE_PREFIX) && key !== CACHE).map((key) => caches.delete(key))),
    ),
  );
});

// A waiting build takes over only when a page asks for it, right at its start (index.html), never under a running app.
self.addEventListener('message', (event) => {
  if (event.data === 'skipWaiting') self.skipWaiting();
});

self.addEventListener('fetch', (event) => {
  const request = event.request;
  if (request.method !== 'GET' || !request.url.startsWith(SCOPE)) return;
  const path = new URL(request.url).pathname.slice(new URL(SCOPE).pathname.length);
  if (request.mode === 'navigate') {
    // Only the app's own page; a branch preview under this scope has its own page and service worker.
    if (path === '' || path === 'index.html') event.respondWith(fromCache('index.html', request));
  } else if (PRECACHED.has(path)) {
    event.respondWith(fromCache(path, request));
  } else if (path.startsWith('composeResources/')) {
    event.respondWith(fromCacheOrFetchAndStore(request));
  }
});

async function fromCache(path, request) {
  const cached = await caches.match(new URL(path, SCOPE).href, { ...MATCH, cacheName: CACHE });
  return cached ?? fetch(request);
}

// Resources outside the first frame (other screens' images, strings): cached on first use, within this build.
async function fromCacheOrFetchAndStore(request) {
  const cache = await caches.open(CACHE);
  const cached = await cache.match(request, MATCH);
  if (cached) return cached;
  const response = await fetch(request);
  if (response.ok && response.type === 'basic') await cache.put(request, response.clone());
  return response;
}
