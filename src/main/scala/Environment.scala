package dev.vanderloureiro

import domain.WorkdaysLive

import zio.ULayer

object Environment {

  val env: ULayer[WorkdaysLive] = WorkdaysLive.live
}
