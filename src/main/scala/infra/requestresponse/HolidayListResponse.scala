package dev.vanderloureiro
package infra.requestresponse

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.LocalDate

case class HolidayListResponse(holidays: List[LocalDate])

object HolidayListResponse {
  implicit val encoder: Encoder[HolidayListResponse] = deriveEncoder
  implicit val decoder: Decoder[HolidayListResponse] = deriveDecoder
}
