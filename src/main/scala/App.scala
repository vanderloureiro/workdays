package dev.vanderloureiro

import zio.http.{handler, Method, Response, Root, Routes, Server}
import zio.ZIOAppDefault

object App extends ZIOAppDefault {

  override def run = Server
    .serve(ApiRoutes.routes)
    .provide(Server.defaultWithPort(8080), Environment.env)
}
