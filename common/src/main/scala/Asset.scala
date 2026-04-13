package io.github.sgtswagrid.assetloader

/**
  * A static asset that can be served by a web server.
  *
  * @param content
  *   The raw bytes from the asset file.
  *
  * @param contentType
  *   The MIME type of the asset. Appears in the
  *   [`Content-Type`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Content-Type)
  *   header. Helps the browser understand how to handle the file.
  *
  * @param eTag
  *   The version hash of the asset. Appears in the
  *   [ETag](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/ETag)
  *   header. Helps the browser determine when a cached asset is up-to-date.
  *
  * @param cacheControl
  *   The caching rules for the asset. Appears in the
  *   [`Cache-Control`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cache-Control)
  *   header. Tells the browser when a cached asset needs to be replaced.
  */
type Asset = (
  content: Array[Byte],
  contentType: String,
  eTag: String,
  cacheControl: String,
)
