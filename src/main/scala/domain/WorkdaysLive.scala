package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

import java.time.LocalDate

case class WorkdaysLive() extends Workdays {
  override def calculateNextWorkadayFrom(
    input: CalculateNextWorkdayInput
  ): ZIO[Workdays, Nothing, CalculateNextWorkdayOutput] =
    ZIO.succeed(CalculateNextWorkdayOutput(startDate = LocalDate.now(), LocalDate.now()))
}

object WorkdaysLive {

  val layer: ULayer[WorkdaysLive] = ZLayer.succeed(WorkdaysLive())
}
