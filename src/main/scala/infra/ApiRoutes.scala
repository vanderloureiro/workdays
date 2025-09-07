package dev.vanderloureiro
package infra

import Environment.AppEnv
import domain.*
import infra.requestresponse.{BooleanResponse, HolidayListResponse, NextWorkdayRequest, NextWorkdayResponse}

import io.scalaland.chimney.dsl.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.*
import zio.http.{Response, Routes}

import java.time.LocalDate

object ApiRoutes {

  val isHolidayRoute: ZServerEndpoint[AppEnv, Any] = endpoint.get
    .in("api" / "holidays" / "is-holiday")
    .in(query[String]("date"))
    .out(jsonBody[BooleanResponse])
    .serverLogicSuccess(date => Holidays.isHoliday(date).map(result => BooleanResponse(result)))

  val getHolidaysRoute: ZServerEndpoint[AppEnv, Any] = endpoint.get
    .in("api" / "holidays")
    .in(query[String]("year"))
    .out(jsonBody[HolidayListResponse])
    .serverLogicSuccess(year =>
      Holidays.getHolidays(year.toInt).map(result => HolidayListResponse(result))
    )

  val calculateWorkdayRoute: ZServerEndpoint[AppEnv, Any] = endpoint.post
    .in("api" / "workdays")
    .in(jsonBody[NextWorkdayRequest])
    .out(jsonBody[NextWorkdayResponse])
    .serverLogicSuccess(request =>
      Workdays
        .calculateNextWorkadayFrom(request.transformInto[NextWorkdayInput])
        .map(result => result.transformInto[NextWorkdayResponse])
    )

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
