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

  val willItBeAHolidayRoute: Route[Holidays, Nothing] = Method.GET / "api" / "is-holiday" -> handler { (req: Request) =>
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
  val routes: Routes[Holidays, Nothing] = Routes(homeRoute, jsonRoute, willItBeAHolidayRoute)
}
