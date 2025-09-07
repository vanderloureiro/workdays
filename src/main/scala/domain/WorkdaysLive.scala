package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

import java.time.{DayOfWeek, LocalDate, Period}

case class WorkdaysLive(holidaysService: Holidays) extends Workdays {

  override def calculateNextWorkadayFrom(
    input: NextWorkdayInput
  ): ZIO[Workdays, Nothing, NextWorkdayOutput] = for {
    next <- ZIO.succeed(LocalDate.now())
    futureDate = input.startDate.plusDays(input.daysQuantity)
    yearsDiff  = Period.between(input.startDate, futureDate).getYears

    holidays = (0 until yearsDiff - 1).map { year =>
      holidaysService.getHolidays(LocalDate.now().plusYears(year).getYear)
    }
    value = (0 to input.daysQuantity).foldLeft(input.startDate) { (acc, i) =>
      val current = input.startDate.plusDays(i)
      if (
        holidays.contains(current) || current.getDayOfWeek.equals(
          DayOfWeek.SATURDAY
        ) || current.getDayOfWeek.equals(DayOfWeek.SUNDAY)
      ) acc
      else current
    }

    next <- ZIO.succeed(value)
  } yield NextWorkdayOutput(
    startDate = input.startDate,
    daysQuantity = input.daysQuantity,
    nextWorkday = next
  )
}

object WorkdaysLive {

  val layer: ZLayer[Holidays, Nothing, WorkdaysLive] = ZLayer.fromZIO(for {
    holidays <- ZIO.service[Holidays]
  } yield WorkdaysLive(holidays))
}
