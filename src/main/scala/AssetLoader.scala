package io.github.sgtswagrid.assetloader

import java.nio.file.{Files, Path, Paths}
import java.security.MessageDigest
import scala.jdk.CollectionConverters.*

/**
  * A utility for loading and caching static assets so that they can be served
  * by a web server.
  *
  * Only intended for use when the total number of assets is small enough to fit
  * comfortably in memory.
  *
  * Warning: Work with the assumption that all files in `assetsPath` will be
  * made publicly accessible.
  *
  * @param assetsPath
  *   The root directory on the server's file system where assets are stored,
  *   relative to the directory of the running server process.
  */
class AssetLoader private (private val assetsPath: Path):

  /**
    * Retrieves the asset corresponding to the given path, if it exists.
    *
    * @param path
    *   The path to the asset, relative to the `assetsPath`.
    */
  def getAsset(path: String): Option[Asset] = assets.get(path)

  /**
    * A collection of all assets, keyed by relative path. This is loaded lazily
    * on the first call to [[getAsset]], at which point all assets are loaded at
    * once.
    */
  private lazy val assets: Map[String, Asset] = Files
    .walk(assetsPath)
    .iterator
    .asScala
    .filter(Files.isRegularFile(_))
    .map: file =>
      val bytes = Files.readAllBytes(file)
      val path  = assetsPath.relativize(file).toString.replace("\\", "/")
      path -> (bytes, contentType(file), etag(bytes))
    .toMap

  /**
    * Computes a strong ETag for the given bytes using an MD5 hash.
    *
    * @param bytes
    *   The raw file contents to hash.
    *
    * @return
    *   An ETag value as a quoted hex string (e.g. `"d41d8cd98f00b204..."`).
    */
  private def etag(bytes: Array[Byte]): String =
    val hash = MessageDigest.getInstance("MD5").digest(bytes)
    hash.map("%02x".format(_)).mkString("\"", "", "\"")

  /**
    * Determines the content type based on the file extension. Known text types
    * are returned with an explicit charset. For all other extensions, falls
    * back to [[java.nio.file.Files.probeContentType]] using the JVM/OS MIME
    * type registry, or `application/octet-stream` if unrecognised.
    *
    * @param file
    *   The path to the file.
    *
    * @param charset
    *   The name of the [character
    *   encoding](https://en.wikipedia.org/wiki/Character_encoding) used for
    *   text files, defaulting to
    *   [`utf-8`](https://en.wikipedia.org/wiki/UTF-8).
    *
    * @return
    *   The content type as a string, or `application/octet-stream` by default
    *   if the extension is unknown.
    */
  private def contentType(file: Path, charset: String = "utf-8"): String = file
    .getFileName
    .toString
    .split("\\.")
    .lastOption
    .getOrElse("")
    .toLowerCase match

      // Code files
      case "html" | "htm" => s"text/html; charset=$charset"
      case "css"          => s"text/css; charset=$charset"
      case "js" | "mjs"   => s"text/javascript; charset=$charset"
      case "json" | "map" => s"application/json; charset=$charset"
      case "xml"          => s"text/xml; charset=$charset"
      case "txt"          => s"text/plain; charset=$charset"
      case "scala"        => s"text/plain; charset=$charset"
      case "glsl"         => s"text/plain; charset=$charset"
      case "webmanifest"  => s"application/manifest+json; charset=$charset"
      case "wasm"         => "application/wasm"

      // Image files
      case "svg"          => "image/svg+xml"
      case "png"          => "image/png"
      case "jpg" | "jpeg" => "image/jpeg"
      case "gif"          => "image/gif"
      case "webp"         => "image/webp"
      case "avif"         => "image/avif"
      case "ico"          => "image/x-icon"

      // Audio files
      case "mp3"  => "audio/mpeg"
      case "wav"  => "audio/wav"
      case "ogg"  => "audio/ogg"
      case "aac"  => "audio/aac"
      case "opus" => "audio/opus"
      case "flac" => "audio/flac"
      case "m4a"  => "audio/mp4"

      // Font files
      case "woff"  => "font/woff"
      case "woff2" => "font/woff2"
      case "ttf"   => "font/ttf"
      case "otf"   => "font/otf"

      // Everything else; delegate to the JVM/OS MIME type registry.
      // Warning: The behaviour here is OS-dependent.
      case _ => Option(
          Files.probeContentType(file),
        ).getOrElse("application/octet-stream")

/** @see [[AssetLoader]] */
object AssetLoader:

  /**
    * Constructs a new [[AssetLoader]].
    * @param assetsPath
    *   The root directory on the server's file system where assets are stored,
    *   relative to the directory of the running server process.
    */
  def apply(assetsPath: String): AssetLoader =
    new AssetLoader(Paths.get(assetsPath))

  /**
    * Constructs a new [[AssetLoader]].
    *
    * @param assetsPath
    *   The root directory on the server's file system where assets are stored,
    *   relative to the directory of the running server process. Given as a
    *   "/"-separated array of path segments.
    */
  def apply(assetsPath: Iterable[String]): AssetLoader =
    new AssetLoader(Paths.get(assetsPath.mkString("/")))

  /**
    * Constructs a new [[AssetLoader]].
    *
    * @param assetsPath
    *   The root directory on the server's file system where assets are stored,
    *   relative to the directory of the running server process.
    */
  def apply(assetsPath: Path): AssetLoader = new AssetLoader(assetsPath)
