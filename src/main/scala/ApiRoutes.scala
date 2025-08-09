package dev.vanderloureiro

import dev.vanderloureiro.domain.{
  BooleanResponse,
  CalculateNextWorkdayInput,
  CalculateNextWorkdayOutput,
  Holidays,
  Workdays
}
import dev.vanderloureiro.Environment.AppEnv
import sttp.apispec.openapi
import sttp.tapir.*
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.circe.jsonBody
import zio.*
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.generic.auto.*
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.http.endpoint.openapi.OpenAPI
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

  private val endpoints =
    List(tapirEndpoint, getHolidaysRoute, isHolidayRoute, calculateWorkdayRoute)

  // Docs
  val swaggerEndpoint: List[ZServerEndpoint[Any, Any]] =
    SwaggerInterpreter().fromEndpoints[Task](endpoints.map(_.endpoint), "Workday API", "1.0")

  val routes: Routes[AppEnv, Response] = ZioHttpInterpreter().toHttp(
    endpoints
  ) ++ ZioHttpInterpreter().toHttp(swaggerEndpoint)
}
