<div align="center">
  <h1>💾 Asset Loader</h1>
  <p>A simple static asset loader for <a href="https://www.scala-lang.org/">Scala</a> web servers.</p>
  <span>
    <a href="https://github.com/SgtSwagrid/asset-loader/actions/workflows/build-integrity.yml"><img src="https://github.com/SgtSwagrid/asset-loader/actions/workflows/build-integrity.yml/badge.svg" alt="Build status" /></a>
    <a href="https://search.maven.org/artifact/com.alecdorrington/asset-loader_3"><img src="https://img.shields.io/maven-central/v/com.alecdorrington/asset-loader_3.svg" alt="Maven Central" /></a>
    <a href="https://alecdorrington.com/asset-loader"><img src="https://img.shields.io/badge/docs-latest-blue.svg" alt="Documentation" /></a>
  </span>
</div>

<br/><br/>

> "The more you sweat in peace, the less you bleed in war." — Norman Schwarzkopf.

<br/>

## ✔️ Features

This tool is extremely small and minimalistic, with absolutely no bells or whistles.

1. Loads all static assets (e.g. images, stylesheets, scripts, etc.) into memory.
2. Allows retrieval by relative path.
3. Tags each asset with an appropriate [Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Content-Type).
4. Tags each asset with an [ETag](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/ETag) for efficient caching and update detection.

## ⬇️ Installation

Add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.alecdorrington" %% "asset-loader" % "0.2.5"
```

Compiled with Scala `3.8.3`, with no intention to explicitly support older versions.

## ⚙️ Example

This example uses fake `Request` and `Response` types to illustrate the idea in a simple manner.
The details will depend on your choice of web framework (e.g. [Tapir](https://tapir.softwaremill.com/en/latest/) or [http4s](https://http4s.org/)).

```scala
import com.alecdorrington.assetloader.{Asset, AssetLoader}

val assetLoader = AssetLoader(assetsPath = "client/src/main/resources")

def handleRequest(request: Request): Response =
  if request.path.startsWith("assets/") then
    val assetOption: Option[Asset] = assetLoader.getAsset(request.path.dropLeft(7))
    assetOption match
      case Some(asset) => Response(asset)
      case None => Response.NotFound
  else
    // ...
```

## 📡 Server Integration 

Currently, a connector exists for only a single web framework: Tapir.
In principle, any future connectors will be published as separate dependencies with the name `asset-loader-{web-framework}`.
Contributions are welcome!

### Tapir

[Tapir](https://tapir.softwaremill.com/en/latest/) is a library to describe HTTP APIs and expose them as a server. A separate connector is provided to easily create a Tapir endpoint that serves static files from _Asset Loader_. Just add the following additional dependency:

```scala
libraryDependencies += "com.alecdorrington" %% "asset-loader-tapir" % "0.2.5"
```

Observe the following minimal example, using [Netty](https://netty.io/) and [Cats Effect](https://typelevel.org/cats-effect/):

```scala
object Main extends ResourceApp.Forever:

  val assets = AssetService[IO](
    externalPath = "assets",
    internalPath = Paths.get("src/main/resources"),
  )

  def run(args: List[String]) =
    NettyCatsServer
      .io()
      .flatMap: server =>
        val service = server
          .host("0.0.0.0")
          .port("8080")
          .addEndpoints(assets.api)
        Resource.make(service.start())(_.stop()).as(())
```

## 🖥️ Client Versions

All of the above dependencies are exclusively for the JVM.
However, you may wish to access the non-JVM-specific functionality from the client as well.
For this reason, each aforementioned dependency is published with a common part that is cross-compiled.

These can be installed as follows:

```scala
libraryDependencies += "com.alecdorrington" %%% "asset-loader-common" % "0.2.5"
```

```scala
libraryDependencies += "com.alecdorrington" %%% "asset-loader-tapir-common" % "0.2.5"
```

Note that you don't need to explicitly include the above if you only use this library on the server.


## 👁️ See also

- See [Scala Website Template](https://github.com/SgtSwagrid/scala-website-template) for an example template which uses _Asset Loader_ to build a full stack website.
- See [Page Loader](https://github.com/SgtSwagrid/page-loader) for a similar library which loads [Scala.js](https://www.scala-js.org/) webpages instead of static assets.
- This library was made using [Scala Library Template](https://github.com/SgtSwagrid/scala-library-template).
