package dev.vanderloureiro
package domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

case class BooleanResponse(val value: Boolean)

object BooleanResponse {
  implicit val encoder: Encoder[BooleanResponse] = deriveEncoder
  implicit val decoder: Decoder[BooleanResponse] = deriveDecoder
}
