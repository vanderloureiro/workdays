package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class HolidaysLive() extends Holidays {

  override def isHoliday(date: String): ZIO[Holidays, Nothing, BooleanResponse] = {
    for {
      date <- parseDate(date)
      holidays <- calcHolidays(date.getYear)
      isHoliday <- ZIO.succeed(holidays.contains(date))
    } yield BooleanResponse(isHoliday)
  }

  private def parseDate(date: String): ZIO[Holidays, Nothing, LocalDate] = {
    for {
      formatter <- ZIO.succeed(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
      parts <- ZIO.succeed(date.split("-"))
      finalDate <- ZIO.attempt {
        LocalDate.parse(date, formatter)
      }.catchAll(e => ZIO.log(s"error: ${e}").as(LocalDate.now()))
    } yield finalDate
  }

  private def calcHolidays(year: Int): ZIO[Any, Nothing, List[LocalDate]] = {
    val diaPrimeiro = LocalDate.of(year, 1, 1)
    val carnaval = carnival(year)
    val sextaFeiraSanta = goodFriday(year)
    val corpusCristi = corpusChristi(year)
    val tiradentes = LocalDate.of(year, 4, 21)
    val independencia = LocalDate.of(year, 9, 7)
    val nossaSenhora = LocalDate.of(year, 10, 12)
    val finados = LocalDate.of(year, 11, 2)
    val proclamacaoRepublica = LocalDate.of(year, 12, 15)
    val conscienciaNegra = LocalDate.of(year, 11, 20)
    val natal = LocalDate.of(year, 12, 25)

    val holidays = List(diaPrimeiro, carnaval, sextaFeiraSanta, corpusCristi, tiradentes, independencia, nossaSenhora,
      finados, proclamacaoRepublica, conscienciaNegra, natal)
    ZIO.succeed(holidays)
  }

  // Meeus/Jones/Butcher algorithm
  private def easterDate(year: Int): LocalDate = {
    val a = year % 19
    val b = year / 100
    val c = year % 100
    val d = b / 4
    val e = b % 4
    val f = (b + 8) / 25
    val g = (b - f + 1) / 3
    val h = (19 * a + b - d - g + 15) % 30
    val i = c / 4
    val k = c % 4
    val l = (32 + 2 * e + 2 * i - h - k) % 7
    val m = (a + 11 * h + 22 * l) / 451
    val month = (h + l - 7 * m + 114) / 31
    val day = ((h + l - 7 * m + 114) % 31) + 1

    LocalDate.of(year, month, day)
  }

  private def carnival(year: Int): LocalDate = easterDate(year).minusDays(47)

  private def goodFriday(year: Int): LocalDate = easterDate(year).minusDays(2)

  private def corpusChristi(year: Int): LocalDate = easterDate(year).plusDays(60)

}

object HolidaysLive {
  val live: ULayer[HolidaysLive] = ZLayer.succeed(HolidaysLive())
}
