package dev.vanderloureiro

import zio.http.{Method, Response, Root, Routes, Server, handler}
import zio.ZIOAppDefault

object App extends ZIOAppDefault {

  val home = Method.GET / Root -> handler(Response.json("""{"title":"Workdays API"}"""))

  val app = Routes(home)

  override def run = Server.serve(app).provideLayer(Server.defaultWithPort(8080))
}
