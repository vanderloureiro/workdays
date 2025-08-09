package dev.vanderloureiro
package domain

import zio.{IO, ZIO}

import java.time.LocalDate

trait Workdays {

  def calculateNextWorkadayFrom(
    input: CalculateNextWorkdayInput
  ): ZIO[Workdays, Nothing, CalculateNextWorkdayOutput]
}

object Workdays {

  def calculateNextWorkadayFrom(
    input: CalculateNextWorkdayInput
  ): ZIO[Workdays, Nothing, CalculateNextWorkdayOutput] =
    ZIO.serviceWithZIO[Workdays](_.calculateNextWorkadayFrom(input))
}

case class CalculateNextWorkdayInput(
  val startDate: LocalDate,
  val daysQuantity: Int
)

case class CalculateNextWorkdayOutput(
  val startDate: LocalDate,
  val nextWorkday: LocalDate
)
