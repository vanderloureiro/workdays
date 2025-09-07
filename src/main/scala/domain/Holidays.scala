package dev.vanderloureiro
package domain

import zio.{ZIO, ZLayer}

import java.time.LocalDate

trait Holidays {

  def isHoliday(date: String): ZIO[Holidays, Nothing, Boolean]

  def getHolidays(year: Int): ZIO[Holidays, Nothing, List[LocalDate]]
}

object Holidays {
  def isHoliday(date: String): ZIO[Holidays, Nothing, Boolean] =
    ZIO.serviceWithZIO[Holidays](_.isHoliday(date))

  def getHolidays(year: Int): ZIO[Holidays, Nothing, List[LocalDate]] =
    ZIO.serviceWithZIO[Holidays](_.getHolidays(year))
}
