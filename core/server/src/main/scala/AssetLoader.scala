package com.alecdorrington.assetloader

import java.nio.file.*
import java.security.MessageDigest
import scala.jdk.CollectionConverters.*

/**
  * A utility for loading and caching static assets so that they can be served
  * by a web server.
  *
  * Only intended for use when the total number of assets is small enough to fit
  * comfortably in memory.
  *
  * Warning: Work with the assumption that all files in `assetPath` will be made
  * publicly accessible.
  *
  * @param assetPath
  *   The root directory on the server's file system where assets are stored,
  *   relative to the directory of the running server process.
  *
  * @param maxAge
  *   The maximum age (in seconds) for which an asset should be considered fresh
  *   for caching purposes. Caching is disabled by default (`0`).
  */
class AssetLoader private (
  private val assetPath: Path,
  private val maxAge: Int = 0,
):

  /**
    * Retrieves the asset corresponding to the given path, if it exists.
    *
    * @param path
    *   The path to the asset, relative to the `assetPath`.
    */
  def getAsset(path: String): Option[Asset] = assets.get(path)

  /**
    * Retrieves the asset corresponding to the given path, if it exists.
    *
    * @param path
    *   The path to the asset, relative to the `assetPath`. Given as a
    *   "/"-separated array of path segments.
    */
  def getAsset(path: Iterable[String]): Option[Asset] =
    getAsset(path.mkString("/"))

  /**
    * Retrieves the asset corresponding to the given path, if it exists.
    *
    * @param path
    *   The path to the asset, relative to the `assetPath`.
    */
  def getAsset(path: Path): Option[Asset] =
    getAsset(path.toString.replace("\\", "/"))

  /**
    * A collection of all assets, keyed by relative path. This is loaded lazily
    * on the first call to [[getAsset]], at which point all assets are loaded at
    * once.
    */
  private lazy val assets: Map[String, Asset] = Files
    .walk(assetPath)
    .iterator
    .asScala
    .filter(Files.isRegularFile(_))
    .map: file =>
      val bytes = Files.readAllBytes(file)
      val path  = assetPath.relativize(file).toString.replace("\\", "/")
      path -> (bytes, MIME.contentType(file), eTag(bytes), cacheControl)
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
  private def eTag(bytes: Array[Byte]): String =
    val hash = MessageDigest.getInstance("MD5").digest(bytes)
    hash.map("%02x".format(_)).mkString("\"", "", "\"")

  /** The `Cache-Control` header value based on the chosen `maxAge`. */
  private lazy val cacheControl: String =
    if maxAge <= 0 then "no-cache" else s"public, max-age=$maxAge}"

/** @see [[AssetLoader]] */
object AssetLoader:

  /**
    * Constructs a new [[AssetLoader]].
    *
    * @param assetPath
    *   The root directory on the server's file system where assets are stored,
    *   relative to the directory of the running server process.
    *
    * @param maxAge
    *   The maximum age (in seconds) for which an asset should be considered
    *   fresh for caching purposes. Set to `0` to disable caching.
    */
  def apply(assetPath: String, maxAge: Int): AssetLoader =
    new AssetLoader(Paths.get(assetPath))

  /**
    * Constructs a new [[AssetLoader]].
    *
    * @param assetPath
    *   The root directory on the server's file system where assets are stored,
    *   relative to the directory of the running server process. Given as a
    *   "/"-separated array of path segments.
    *
    * @param maxAge
    *   The maximum age (in seconds) for which an asset should be considered
    *   fresh for caching purposes. Set to `0` to disable caching.
    */
  def apply(assetPath: Iterable[String], maxAge: Int): AssetLoader =
    new AssetLoader(Paths.get(assetPath.mkString("/")))

  /**
    * Constructs a new [[AssetLoader]].
    *
    * @param assetPath
    *   The root directory on the server's file system where assets are stored,
    *   relative to the directory of the running server process.
    *
    * @param maxAge
    *   The maximum age (in seconds) for which an asset should be considered
    *   fresh for caching purposes. Set to `0` to disable caching.
    */
  def apply(assetPath: Path, maxAge: Int): AssetLoader =
    new AssetLoader(assetPath)
