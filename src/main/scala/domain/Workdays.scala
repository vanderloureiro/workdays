package dev.vanderloureiro
package domain

import zio.{ZIO, ZLayer}

trait Workdays {

  def isWorkday(date: String): ZIO[Workdays, Nothing, BooleanResponse]
}

object Workdays {
  def isWorkday(date: String): ZIO[Workdays, Nothing, BooleanResponse] =
    ZIO.serviceWithZIO[Workdays](_.isWorkday(date))
}
