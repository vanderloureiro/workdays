package dev.vanderloureiro
package infra.requestresponse

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

case class BooleanResponse(val value: Boolean)

object BooleanResponse {
  implicit val encoder: Encoder[BooleanResponse] = deriveEncoder
  implicit val decoder: Decoder[BooleanResponse] = deriveDecoder
}
