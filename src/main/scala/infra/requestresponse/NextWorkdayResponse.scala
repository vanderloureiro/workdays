package dev.vanderloureiro
package infra.requestresponse

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.time.LocalDate

case class NextWorkdayResponse(
  startDate: LocalDate,
  daysQuantity: Int,
  nextWorkday: LocalDate
)

object NextWorkdayResponse {
  implicit val encoder: Encoder[NextWorkdayResponse] = deriveEncoder
  implicit val decoder: Decoder[NextWorkdayResponse] = deriveDecoder
}
