package dev.vanderloureiro

import domain.{Holidays, HolidaysLive, Workdays, WorkdaysLive}

import zio.ULayer
import zio.http.endpoint.openapi.OpenAPI

object Environment {

  type AppEnv = Holidays with Workdays

  val env: ULayer[AppEnv] = HolidaysLive.layer ++ WorkdaysLive.layer
}
