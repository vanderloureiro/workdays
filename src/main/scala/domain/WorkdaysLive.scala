package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

import java.time.LocalDate

case class WorkdaysLive() extends Workdays {

  override def isWorkday(date: String): ZIO[Workdays, Nothing, BooleanResponse] = {
    for {
      _ <- parseDate(date)
    } yield BooleanResponse(true)
  }

  private def parseDate(date: String): ZIO[Workdays, Nothing, LocalDate] =
    ZIO.attempt(LocalDate.parse(date))
      .catchAll(e => ZIO.log(s"error: ${e}").as(LocalDate.now()))
}

object WorkdaysLive {
  val live: ULayer[WorkdaysLive] = ZLayer.succeed(WorkdaysLive())
}
