package com.cats

import scala.xml.Elem

case class CatImage(url:String, id:String)

object CatImage {
  def apply(xml:Elem):CatImage = {
    val url = (xml \\ "image" \ "url").text.trim
    val id = (xml \\ "image" \"id").text.trim
    CatImage(url, id)
  }
}
