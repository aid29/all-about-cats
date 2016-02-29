package com.cats

import cats.data.Xor
import dispatch._

import scala.concurrent.{ExecutionContext, Future}

class CatsHttpApi(httpClient:Http, catApiBaseUri:Req, catFactApiBaseUri:Req)(implicit executor: ExecutionContext) extends CatsApi{
  override def catImage = {
    val req: Req = (catApiBaseUri / "images" / "get").addQueryParameter("format","xml")
    httpClient(req OK as.xml.Elem).either.map(either=>{
      Xor.fromEither(either).map(CatImage(_)).leftMap({
        case StatusCode(code) => ApiError(code)
        case t: Throwable => Error(t)
      })
    })
  }

  override def catCategories:Future[Xor[CatsApiError,List[CatCategory]]] = {
    val req: Req = (catApiBaseUri / "categories" / "list")
    httpClient(req OK as.xml.Elem).either.map(either=>{
      Xor.fromEither(either).map(CatCategory.fromXml(_)).leftMap({
        case StatusCode(code) => ApiError(code)
        case t: Throwable => Error(t)
      })
    })
  }

  override def catFact: dispatch.Future[Xor[CatsApiError, String]] = {
    val req: Req = (catFactApiBaseUri / "facts")
    httpClient(req OK as.String).either.map(either => {
      Xor.fromEither(either).leftMap({
        case StatusCode(code) => ApiError(code)
        case t: Throwable => Error(t)
      }).flatMap(CatsJsonHelper.extractFact(_))
    })
  }
}

trait CatsApi {
  def catImage:Future[Xor[CatsApiError, CatImage]]
  def catCategories:Future[Xor[CatsApiError,List[CatCategory]]]
  def catFact:Future[Xor[CatsApiError, String]]
}
