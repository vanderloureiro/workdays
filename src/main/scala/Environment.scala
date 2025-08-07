package dev.vanderloureiro

import domain.HolidaysLive

import zio.ULayer

object Environment {

  val env: ULayer[HolidaysLive] = HolidaysLive.live
}
