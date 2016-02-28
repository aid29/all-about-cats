package com.cats.fake

import com.twitter.finagle.Http
import com.twitter.finagle.http.Status
import io.finch._

import scala.xml.Elem

class FakeCatApi(port:Int) {

  private val getImageEndpoint = get("images" / "get")

  private def imageEndpointReturn(catImgXmlRes:Elem): Endpoint[String] = getImageEndpoint {
      Ok(catImgXmlRes.toString())
    }

  private def failedImageEndpointWith(statusCode:Int): Endpoint[String] = getImageEndpoint {
    if(statusCode == 200) {
      Ok("ok")
    } else {
      Output.failure(new Exception("fake failed"), Status(statusCode))
    }
  }

  def withCatImageXml(res:Elem)(block: => Unit) {
    val server = Http.serve(s":$port", imageEndpointReturn(res).toService)
    block
    server.close()
  }

  def failedOn(statusCode:Int)(block: => Unit): Unit = {
    val server = Http.serve(s":$port", failedImageEndpointWith(statusCode).toService)
    block
    server.close()
  }
}

