package dev.vanderloureiro

import domain.{Holidays, HolidaysLive, Workdays, WorkdaysLive}

import zio.ULayer

object Environment {

  type AppEnv = Holidays with Workdays
  
  val env: ULayer[AppEnv] = HolidaysLive.layer ++ WorkdaysLive.layer
}
