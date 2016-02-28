package com.cats

import scala.xml.{Node, Elem}

case class CatCategory(id:Long, name:String)

object CatCategory {
  def fromXml(elem:Elem):List[CatCategory] = {
    (elem \\ "category").toSeq.map(node=>buildCatCatcategory(node)).toList.sortBy(_.name)
  }

  private def buildCatCatcategory(elem:Node):CatCategory = {
    CatCategory((elem \ "id").text.toLong, (elem \ "name").text)
  }
}
