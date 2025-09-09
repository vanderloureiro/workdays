package dev.vanderloureiro
package domain

import zio.Scope
import zio.test.*
import zio.test.Assertion.*
import java.time.LocalDate

object WorkdaysSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment & Scope, Any] = suite("success")(
    success
  )

  private val success = test("Get next workday") {

    val result = Workdays.calculateNextWorkadayFrom(NextWorkdayInput(LocalDate.of(2025, 9, 8), 4))
      .provideLayer(HolidaysLive.layer >>> WorkdaysLive.layer)

    assertZIO(result)(
      hasField[NextWorkdayOutput, LocalDate]("nextWorkday", _.nextWorkday, equalTo(LocalDate.of(2025, 9, 12)))
    )
  }
}
