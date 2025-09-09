package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class HolidaysLive() extends Holidays {

  override def isHoliday(
    date: String
  ): ZIO[Holidays, Nothing, Boolean] =
    for {
      date      <- parseDate(date)
      holidays  <- getHolidays(date.getYear)
      isHoliday <- ZIO.succeed(holidays.contains(date))
    } yield isHoliday

  private def parseDate(date: String): ZIO[Holidays, Nothing, LocalDate] =
    for {
      formatter <- ZIO.succeed(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
      parts     <- ZIO.succeed(date.split("-"))
      finalDate <- ZIO
        .attempt {
          LocalDate.parse(date, formatter)
        }
        .catchAll(e => ZIO.log(s"error: ${e}").as(LocalDate.now()))
    } yield finalDate

  def getHolidays(year: Int): ZIO[Any, Nothing, List[LocalDate]] = {
    val newYearsDay = LocalDate.of(year, 1, 1) // Dia Primeiro
    val carnival = calcCarnivalDate(year) // Carnaval
    val goodFriday = calcGoodFridayDate(year) // Sexta-feira Santa
    val corpusChristi = calcCorpusChristiDate(year) // Corpus Christi
    val tiradentesDay = LocalDate.of(year, 4, 21) // Tiradentes
    val independenceDay = LocalDate.of(year, 9, 7) // Independência do Brasil
    val ourLadyOfAparecida = LocalDate.of(year, 10, 12) // Nossa Senhora Aparecida
    val allSoulsDay = LocalDate.of(year, 11, 2) // Finados
    val republicProclamation = LocalDate.of(year, 11, 15) // Proclamação da República
    val blackAwarenessDay = LocalDate.of(year, 11, 20) // Consciência Negra
    val christmasDay = LocalDate.of(year, 12, 25) // Natal

    val holidays = List(
      newYearsDay,
      carnival,
      goodFriday,
      corpusChristi,
      tiradentesDay,
      independenceDay,
      ourLadyOfAparecida,
      allSoulsDay,
      republicProclamation,
      blackAwarenessDay,
      christmasDay
    )
    ZIO.succeed(holidays)
  }

  // Meeus/Jones/Butcher algorithm
  private def easterDate(year: Int): LocalDate = {
    val a     = year                         % 19
    val b     = year / 100
    val c     = year                         % 100
    val d     = b / 4
    val e     = b                            % 4
    val f     = (b + 8) / 25
    val g     = (b - f + 1) / 3
    val h     = (19 * a + b - d - g + 15)    % 30
    val i     = c / 4
    val k     = c                            % 4
    val l     = (32 + 2 * e + 2 * i - h - k) % 7
    val m     = (a + 11 * h + 22 * l) / 451
    val month = (h + l - 7 * m + 114) / 31
    val day   = ((h + l - 7 * m + 114)       % 31) + 1

    LocalDate.of(year, month, day)
  }

  private def calcCarnivalDate(year: Int): LocalDate = easterDate(year).minusDays(47)

  private def calcGoodFridayDate(year: Int): LocalDate = easterDate(year).minusDays(2)

  private def calcCorpusChristiDate(year: Int): LocalDate =
    easterDate(year).plusDays(60)

}

object HolidaysLive {
  val layer: ULayer[HolidaysLive] = ZLayer.succeed(HolidaysLive())
}
