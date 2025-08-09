package dev.vanderloureiro

import dev.vanderloureiro.domain.{
  BooleanResponse,
  CalculateNextWorkdayInput,
  CalculateNextWorkdayOutput,
  Holidays,
  Workdays
}
import dev.vanderloureiro.Environment.AppEnv
import sttp.tapir.*
import sttp.tapir.json.circe.jsonBody
import zio.*
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.generic.auto.*
import sttp.tapir.ztapir.ZServerEndpoint
import zio.http.{Response, Routes}

import java.time.LocalDate

object ApiRoutes {

  val isHolidayRoute: ZServerEndpoint[AppEnv, Any] = endpoint.get
    .in("api" / "holidays" / "is-holiday")
    .in(query[String]("date"))
    .out(jsonBody[BooleanResponse])
    .serverLogicSuccess(date => Holidays.isHoliday(date))

  val getHolidaysRoute: ZServerEndpoint[AppEnv, Any] = endpoint.get
    .in("api" / "holidays")
    .in(query[String]("year"))
    .out(jsonBody[List[LocalDate]])
    .serverLogicSuccess(year => Holidays.getHolidays(year.toInt))

  val calculateWorkdayRoute: ZServerEndpoint[AppEnv, Any] = endpoint.post
    .in("api" / "workdays")
    .in(jsonBody[CalculateNextWorkdayInput])
    .out(jsonBody[CalculateNextWorkdayOutput])
    .serverLogicSuccess(request => Workdays.calculateNextWorkadayFrom(request))

  val tapirEndpoint: ZServerEndpoint[AppEnv, Any] = endpoint.get
    .in("api" / "health")
    .out(jsonBody[BooleanResponse])
    .serverLogicSuccess(_ => ZIO.succeed(BooleanResponse(true)))

  val routes: Routes[AppEnv, Response] = ZioHttpInterpreter().toHttp(
    List(tapirEndpoint, getHolidaysRoute, isHolidayRoute, calculateWorkdayRoute)
  )

}
