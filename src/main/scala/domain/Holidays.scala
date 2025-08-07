package dev.vanderloureiro
package domain

import zio.{ZIO, ZLayer}

trait Holidays {

  def isHoliday(date: String): ZIO[Holidays, Nothing, BooleanResponse]
}

object Holidays {
  def isHoliday(date: String): ZIO[Holidays, Nothing, BooleanResponse] =
    ZIO.serviceWithZIO[Holidays](_.isHoliday(date))
}
