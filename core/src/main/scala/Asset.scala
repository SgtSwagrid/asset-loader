package io.github.sgtswagrid.assetloader

/**
  * A static asset that can be served by a web server.
  *
  * @param content
  *   The raw bytes of the asset file.
  *
  * @param contentType
  *   The MIME type of the asset, used for the
  *   [`Content-Type`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Content-Type)
  *   header. Helps the browser understand how to handle the file.
  *
  * @param etag
  *   An
  *   [ETag](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/ETag)
  *   for the asset, to identify the version for caching purposes.
  *
  * @param cacheControl
  *   Instructions for how the asset may be cached, used for the
  *   [`Cache-Control`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cache-Control)
  *   header.
  */
type Asset = (
  content: Array[Byte],
  contentType: String,
  etag: String,
  cacheControl: String,
)
