package dev.vanderloureiro

import zio.http.{Method, Response, Root, Routes, Server, handler}
import zio.ZIOAppDefault

object App extends ZIOAppDefault {
  
  override def run = Server.serve(ApiRoutes.routes).provide(Server.defaultWithPort(8080), Environment.env)
}
