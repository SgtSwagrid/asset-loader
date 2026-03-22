# 💾 asset-loader

[![Build status](https://github.com/SgtSwagrid/asset-loader/actions/workflows/ci.yml/badge.svg)](https://github.com/SgtSwagrid/asset-loader/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.sgtswagrid/asset-loader_3.svg)](https://search.maven.org/artifact/io.github.sgtswagrid/asset-loader_3)

A simple static asset loader for [Scala](https://www.scala-lang.org/) web servers.

## ✔️ Features

This tool is extremely small and minimalistic, with absolutely no bells or whistles.

1. Loads all static assets (e.g. images, stylesheets, scripts, etc.) into memory.
2. Allows retrieval by relative path.
3. Tags each asset with an appropriate [Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Content-Type).
4. Tags each asset with an [ETag](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/ETag) for efficient caching and update detection.

## ⬇️ Installation

Add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.sgtswagrid" %% "asset-loader" % "0.1.2"
```

Compiled with Scala `3.8.2`, with no intention to explicitly support older versions.

## ⚙️ Example

This example uses fake `Request` and `Response` types to illustrate the idea in a simple manner.
The details will depend on your choice of web framework (e.g. [Tapir](https://tapir.softwaremill.com/en/latest/) or [http4s](https://http4s.org/)).

```scala
import io.github.sgtswagrid.assetloader.{Asset, AssetLoader}

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
