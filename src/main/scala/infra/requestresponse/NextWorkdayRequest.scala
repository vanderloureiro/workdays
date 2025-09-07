package dev.vanderloureiro
package infra.requestresponse

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.time.LocalDate

case class NextWorkdayRequest(
  startDate: LocalDate,
  daysQuantity: Int
)

object NextWorkdayRequest {
  implicit val encoder: Encoder[NextWorkdayRequest] = deriveEncoder
  implicit val decoder: Decoder[NextWorkdayRequest] = deriveDecoder
}
