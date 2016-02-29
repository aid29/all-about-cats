package com.cats.fake

import com.twitter.finagle.{ListeningServer, Service, Http}
import com.twitter.finagle.http.{Response, Request, Status}
import io.finch._

import scala.xml.Elem

class FakeCatApi(port:Int) {
  type HttpStatusCode = Int
  private var server: ListeningServer = null
  private val emptyXmlResponse: Elem = <response></response>

  private var imageEndpointResult:(HttpStatusCode, Elem) = (200, emptyXmlResponse)
  private var categoriesEndpointResult:(HttpStatusCode, Elem) = (200, emptyXmlResponse)
  private var factsEndpointResult:(HttpStatusCode, String) = (200, "{}")

  private def endpointResult[T](statusCode:HttpStatusCode, response:T) = {
    if(statusCode == 200) {
      Ok(response.toString)
    } else {
      Output.failure(new Exception("fake failed"), Status(statusCode))
    }
  }

  def start() = {
    val getImageEndpoint = get("images" / "get") {
      endpointResult(imageEndpointResult._1, imageEndpointResult._2)
    }
    val categoriesEndpoint = get("categories" / "list"){
      endpointResult(categoriesEndpointResult._1, categoriesEndpointResult._2)
    }
    val factsEndpoint = get("facts"){
      endpointResult(factsEndpointResult._1, factsEndpointResult._2)
    }

    server = Http.serve(s":$port", (getImageEndpoint :+: categoriesEndpoint :+: factsEndpoint).toService)
  }

  def stop() = {
    server.close()
  }

  def withCatImageXml(res:Elem)(block: => Unit) {
    imageEndpointResult = (200, res)
    block
  }

  def withCatCategoriesXml(res:Elem)(block: => Unit) {
    categoriesEndpointResult = (200, res)
    block
  }

  def withCatFactJson(json:String)(block: => Unit) {
    factsEndpointResult = (200, json)
    block
  }

  def failedOnCatImage(statusCode:Int)(block: => Unit): Unit = {
    imageEndpointResult = (statusCode, emptyXmlResponse)
    block
  }

  def failedOnCatCategories(statusCode:Int)(block: => Unit): Unit = {
    categoriesEndpointResult = (statusCode, emptyXmlResponse)
    block
  }

  def failedOnCatFact(statusCode:Int)(block: => Unit): Unit = {
    factsEndpointResult = (statusCode, "{}")
    block
  }
}

