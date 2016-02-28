package com.cats.fake

import com.twitter.finagle.Http
import io.finch._

import scala.xml.Elem

class FakeCatApi(port:Int) {

  private def imageApi(catImgXmlRes:Elem): Endpoint[String] = get("images" / "get") {
    Ok(catImgXmlRes.toString())
  }

  def withCatImageXml(res:Elem)(block: => Unit) {
    val server = Http.serve(s":$port", imageApi(res).toService)
    block
    server.close()
  }

}
