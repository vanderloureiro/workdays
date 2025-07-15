package dev.vanderloureiro

import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object App extends ZIOAppDefault {

  override def run: ZIO[ZIOAppArgs & Scope, Any, Any] = ZIO.succeed(println("ZIO Workdays App"))
}
