package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

case class WorkdaysLive() extends Workdays {

  override def isWorkday(date: String): ZIO[Workdays, Nothing, BooleanResponse] = ZIO.succeed(BooleanResponse(true))
}

object WorkdaysLive {
  val live: ULayer[WorkdaysLive] = ZLayer.succeed(WorkdaysLive())
}
