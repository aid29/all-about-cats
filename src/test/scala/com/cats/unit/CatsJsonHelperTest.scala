package com.cats.unit

import cats.data.Xor
import com.cats.{JsonParseError, CatsJsonHelper}
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class CatsJsonHelperTest extends FunSpec {

  it("parse the json to extract the fact") {
    val catFactJson =
      """
        |{
        |    "facts": [
        |        "A cat cannot see directly under its nose."
        |    ],
        |    "success": "true"
        |}
      """.stripMargin
    CatsJsonHelper.extractFact(catFactJson) should be(Xor.right("A cat cannot see directly under its nose."))
  }

  it("return None if parsing the json is failed") {
    val badJson =
      """
        |{
        |   "success": "false"
        |}
        |
      """.stripMargin
    CatsJsonHelper.extractFact(badJson) should be(Xor.Left(JsonParseError(badJson)))
  }

}
