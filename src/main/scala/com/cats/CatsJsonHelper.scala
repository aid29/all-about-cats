package com.cats

import cats.data.Xor
import io.circe.Json
import io.circe.parser._

object CatsJsonHelper {

  def extractFact(factsJson:String): Xor[JsonParseError,String] = {
    val optionStr: Option[String] = parse(factsJson).getOrElse(Json.empty).cursor.downField("facts").map(_.as[List[String]]).flatMap(_.getOrElse(List.empty).headOption)
    Xor.fromOption(optionStr, JsonParseError(factsJson))
  }
}
