package com.alecdorrington.assetloader.tapir

import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter

/**
  * The base trait for all API services in this library. Each service is a
  * thematic grouping of API endpoint implementations.
  *
  * @param serviceName
  *   The name of this service, used in API documentation.
  *
  * @param serviceVersion
  *   The version of this service, used in API documentation.
  *
  * @tparam Capabilities
  *   Capabilities required by this service (e.g. `WebSocket` or `Streams[IO]`).
  *
  * @tparam F
  *   The effect type (e.g. `Future` or `IO`).
  */
trait TapirService[Capabilities, F[_]]
  (
    serviceName: String,
    serviceVersion: String,
  ):

  /** The upper bound on the type of all API endpoints in this service. */
  final type Endpoint = ServerEndpoint[Capabilities, F]

  /** A list of all endpoints implemented in this service. */
  def api: List[Endpoint]

  /** [Swagger](https://swagger.io/) documentation for this service. */
  lazy val docs: List[Endpoint] =
    SwaggerInterpreter().fromServerEndpoints(api, serviceName, serviceVersion)

  /** [Prometheus](https://prometheus.io/) metrics for this service. */
  lazy val metrics: List[Endpoint] =
    List(PrometheusMetrics.default[F]().metricsEndpoint)

  /** All endpoints combined from [[api]], [[docs]], and [[metrics]]. */
  lazy val all: List[Endpoint] = api ++ docs ++ metrics
