package dev.vanderloureiro

import dev.vanderloureiro.domain.Holidays
import zio.*
import zio.http.*

object ApiRoutes {

  val homeRoute: Route[Any, Nothing] =
    Method.GET / Root -> handler(Response.text("Hello World!"))

  // Responds with JSON
  val jsonRoute: Route[Any, Nothing] = {
    Method.GET / "json" -> handler(Response.json("""{"greetings": "Hello World!"}"""))
  }

  val getHolidaysRoute: Route[Holidays, Nothing] = Method.GET / "api" / "holidays" -> handler { (req: Request) =>
    req.query[Int]("year") match {
      case Right(year) => Holidays.getHolidays(year).map(list => Response.json(list.toString()))
      case Left(error) => ZIO.succeed(Response.text(s"Invalid or missing 'year' param: ${error.getMessage}").status(Status.BadRequest))
    }
  }

  val itIsHolidayRoute: Route[Holidays, Nothing] = Method.GET / "api" / "holidays" / "is-holiday" -> handler { (req: Request) =>
    req.query[String]("date") match {
      case Right(dateStr) =>
        for {
          result <- Holidays.isHoliday(dateStr)
          json = s"""{"isHoliday": ${result.value}}"""
        } yield Response.json(json)

      case Left(error) =>
        ZIO.succeed(Response.text(s"Invalid or missing 'date' param: ${error.getMessage}").status(Status.BadRequest))
    }
  }

  // Create HTTP route
  val routes: Routes[Holidays, Nothing] = Routes(homeRoute, jsonRoute, itIsHolidayRoute, getHolidaysRoute)
}
