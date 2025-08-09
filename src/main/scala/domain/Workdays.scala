package dev.vanderloureiro
package domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
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
  startDate: LocalDate,
  daysQuantity: Int
)

object CalculateNextWorkdayInput {
  implicit val encoder: Encoder[CalculateNextWorkdayInput] = deriveEncoder
  implicit val decoder: Decoder[CalculateNextWorkdayInput] = deriveDecoder
}

case class CalculateNextWorkdayOutput(
  startDate: LocalDate,
  nextWorkday: LocalDate
)

object CalculateNextWorkdayOutput {
  implicit val encoder: Encoder[CalculateNextWorkdayOutput] = deriveEncoder
  implicit val decoder: Decoder[CalculateNextWorkdayOutput] = deriveDecoder
}
