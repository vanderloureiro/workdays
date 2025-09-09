package dev.vanderloureiro
package domain

import zio.{Scope, ZIO}
import zio.test.*
import zio.test.Assertion.*

object HolidaysSpec extends ZIOSpecDefault {

    override def spec: Spec[TestEnvironment & Scope, Any] = suite("success")(
      success1,
      success2,
      success3
    )
    
    private val success1 = test("Success getting isHoliday with true response"){

      val result = Holidays.isHoliday("01-01-2028").provideLayer(HolidaysLive.layer)

      assertZIO(result)(isTrue)
    }

    private val success2 = test("Success getting isHoliday with false response") {

      val result = Holidays.isHoliday("02-01-2028").provideLayer(HolidaysLive.layer)

      assertZIO(result)(isFalse)
    }

    private val success3 = test("Success getting holidays list") {

      val result = Holidays.getHolidays(2025).provideLayer(HolidaysLive.layer)

      assertZIO(result)(hasSize(equalTo(11)))
    }
}
