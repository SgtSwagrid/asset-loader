package com.alecdorrington.assetloader

import java.nio.file.{Files, Path}

object MIME:

  /**
    * Determines the content type of a file based on the extension.
    *
    * Resolution is based on the first of the following to succeed:
    *   1. A custom lookup of common web formats.
    *   2. [[java.nio.file.Files.probeContentType]]
    *   3. `application/octet-stream`
    *
    * @param file
    *   The path of the file.
    *
    * @param charset
    *   The name of the [character
    *   encoding](https://en.wikipedia.org/wiki/Character_encoding) used for
    *   text files, defaulting to [utf-8](https://en.wikipedia.org/wiki/UTF-8).
    */
  def contentType(file: Path, charset: String = "utf-8"): String = file
    .getFileName
    .toString
    .split("\\.")
    .lastOption
    .getOrElse("")
    .toLowerCase match

      // Code files:
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

      // Image files:
      case "svg"          => "image/svg+xml"
      case "png"          => "image/png"
      case "jpg" | "jpeg" => "image/jpeg"
      case "gif"          => "image/gif"
      case "webp"         => "image/webp"
      case "avif"         => "image/avif"
      case "ico"          => "image/x-icon"

      // Audio files:
      case "mp3"  => "audio/mpeg"
      case "wav"  => "audio/wav"
      case "ogg"  => "audio/ogg"
      case "aac"  => "audio/aac"
      case "opus" => "audio/opus"
      case "flac" => "audio/flac"
      case "m4a"  => "audio/mp4"

      // Font files:
      case "woff"  => "font/woff"
      case "woff2" => "font/woff2"
      case "ttf"   => "font/ttf"
      case "otf"   => "font/otf"

      // Everything else; delegate to the JVM/OS MIME type registry.
      // Warning: The behaviour here is OS-dependent.
      case _ => Option(
          Files.probeContentType(file),
        ).getOrElse("application/octet-stream")
