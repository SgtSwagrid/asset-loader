package io.github.sgtswagrid.assetloader.tapir

import io.github.sgtswagrid.assetloader.{Asset, AssetLoader}
import java.nio.file.Path
import scala.NamedTuple.DropNames
import sttp.model.StatusCode
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

/**
  * A [Tapir](https://tapir.softwaremill.com/en/latest/) service for serving
  * static assets loaded by an [[AssetLoader]].
  *
  * @param externalPath
  *   The URL path prefix for the endpoint, e.g. "`assets`".
  *
  * @param internalPath
  *   The root directory on the server's file system where assets are stored,
  *   relative to the directory of the running server process, e.g.
  *   "`src/main/resources`".
  *
  * @param maxAge
  *   The maximum age (in seconds) for which an asset should be considered fresh
  *   for caching purposes. Caching is disabled by default (`0`).
  */
class AssetService
  (
    private val externalPath: EndpointInput[Unit],
    private val internalPath: Path,
    private val maxAge: Int = 0,
  ):

  /** The [[AssetLoader]] used to load static assets from the file system. */
  private val assetLoader =
    AssetLoader(assetsPath = internalPath, maxAge = maxAge)

  /**
    * The definition for a Tapir endpoint that serves static files.
    *
    * @see
    *   [[serverEndpoint]] for the server implementation of this endpoint.
    */
  def publicEndpoint
    : PublicEndpoint[(List[String], Option[String]), StatusCode, DropNames[Asset], Any] =
    endpoint
      .get
      .in(externalPath)
      .in(paths)
      .in(header[Option[String]]("If-None-Match"))
      .errorOut(statusCode)
      .out(byteArrayBody)
      .out(header[String]("Content-Type"))
      .out(header[String]("ETag"))
      .out(header[String]("Cache-Control"))

  /**
    * The server implementation for [[publicEndpoint]].
    *
    * @tparam F
    *   The effect type of the server logic, e.g. `Future` or `IO`.
    *
    * @example
    *   Minimal example with [Netty](https://netty.io/) and [Cats
    *   Effect](https://typelevel.org/cats-effect/):
    *   {{{
    *     object Main extends ResourceApp.Forever:
    *
    *       val assets = AssetService(
    *         "assets",
    *         Paths.get("src/main/resources")
    *       )
    *
    *       def run(args: List[String]) =
    *
    *         NettyCatsServer
    *           .io()
    *           .flatMap: server =>
    *             val service = server
    *               .host("0.0.0.0")
    *               .port("8080")
    *               .addEndpoints(assets.serverEndpoint[IO])
    *             Resource.make(service.start())(_.stop()).as(())
    *   }}}
    */
  def serverEndpoint[F[_]]: ServerEndpoint[Any, F] = publicEndpoint
    .serverLogicPure[F]: (path, ifNoneMatch) =>
      assetLoader.getAsset(path) match
        case None => Left(StatusCode.NotFound)
        case Some(asset) if ifNoneMatch.contains(asset.etag) =>
          Left(StatusCode.NotModified)
        case Some(asset) => Right(asset)
