package dev.vanderloureiro

import domain.{Holidays, HolidaysLive}

import zio.ULayer

object Environment {

  type AppEnv = Holidays
  val env: ULayer[HolidaysLive] = HolidaysLive.live
}
