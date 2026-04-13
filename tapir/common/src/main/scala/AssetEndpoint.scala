package io.github.sgtswagrid.assetloader.tapir

import io.github.sgtswagrid.assetloader.Asset
import scala.NamedTuple.DropNames
import sttp.model.StatusCode
import sttp.tapir.*

/**
  * A [Tapir](https://tapir.softwaremill.com/en/latest/) endpoint definition for
  * serving static assets.
  *
  * This is the cross-platform base for [[AssetService]], which adds the
  * server-side logic on JVM.
  *
  * @param externalPath
  *   The URL path prefix for the endpoint, e.g. "`assets`".
  */
class AssetEndpoint(private val externalPath: EndpointInput[Unit]):

  /**
    * The definition for a Tapir endpoint that serves static files.
    *
    * @see
    *   [[AssetService.serverEndpoint]] for the server implementation.
    */
  def publicEndpoint
    : PublicEndpoint[
      (List[String], Option[String]),
      StatusCode,
      DropNames[Asset],
      Any,
    ] = endpoint
    .get
    .in(externalPath)
    .in(paths)
    .in(header[Option[String]]("If-None-Match"))
    .errorOut(statusCode)
    .out(byteArrayBody)
    .out(header[String]("Content-Type"))
    .out(header[String]("ETag"))
    .out(header[String]("Cache-Control"))
