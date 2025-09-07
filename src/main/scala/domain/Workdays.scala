package dev.vanderloureiro
package domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import zio.{IO, ZIO}

import java.time.LocalDate

trait Workdays {

  def calculateNextWorkadayFrom(
    input: NextWorkdayInput
  ): ZIO[Workdays, Nothing, NextWorkdayOutput]
}

object Workdays {

  def calculateNextWorkadayFrom(
    input: NextWorkdayInput
  ): ZIO[Workdays, Nothing, NextWorkdayOutput] =
    ZIO.serviceWithZIO[Workdays](_.calculateNextWorkadayFrom(input))
}

case class NextWorkdayInput(
  startDate: LocalDate,
  daysQuantity: Int
)

case class NextWorkdayOutput(
  startDate: LocalDate,
  daysQuantity: Int,
  nextWorkday: LocalDate
)
