// @see http://www.pwabuilder.com/generator (Build Service Worker => Offline copy of pages)

// This is the "Offline copy of pages" service worker

// Add this below content to your HTML page, or add the js file to your page at the very top to register sercie worker
if (navigator.serviceWorker.controller) {
  console.log('[PWA Builder] active service worker found, no need to register');
} else {
  // Register the ServiceWorker
  navigator.serviceWorker.register('service-worker.js', {
    scope: './'
  }).then(function (reg) {
    console.log('Service worker has been registered for scope:' + reg.scope);
  });
}
