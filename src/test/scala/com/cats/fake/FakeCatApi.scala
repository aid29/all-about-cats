package com.cats.fake

import com.twitter.finagle.Http
import com.twitter.finagle.http.Status
import io.finch._

import scala.xml.Elem

class FakeCatApi(port:Int) {

  private val getImageEndpoint = get("images" / "get")
  private val categoriesEndpoint = get("categories" / "list")

  private def failedImageEndpointWith(statusCode:Int): Endpoint[String] = getImageEndpoint {
    if(statusCode == 200) {
      Ok("ok")
    } else {
      Output.failure(new Exception("fake failed"), Status(statusCode))
    }
  }

  private def failedCategoriesEndpointWith(statusCode:Int): Endpoint[String] = categoriesEndpoint {
    if(statusCode == 200) {
      Ok("ok")
    } else {
      Output.failure(new Exception("fake failed"), Status(statusCode))
    }
  }

  def withCatImageXml(res:Elem)(block: => Unit) {
    val server = Http.serve(s":$port", (getImageEndpoint{Ok(res.toString())}).toService)
    block
    server.close()
  }

  def withCatCategoriesXml(res:Elem)(block: => Unit) {
    val server = Http.serve(s":$port", (categoriesEndpoint{Ok(res.toString())}).toService)
    block
    server.close()
  }

  def failedOnCatImage(statusCode:Int)(block: => Unit): Unit = {
    val server = Http.serve(s":$port", failedImageEndpointWith(statusCode).toService)
    block
    server.close()
  }

  def failedOnCatCategories(statusCode:Int)(block: => Unit): Unit = {
    val server = Http.serve(s":$port", failedCategoriesEndpointWith(statusCode).toService)
    block
    server.close()
  }
}

