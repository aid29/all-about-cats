package com.cats

import cats.data.Xor
import dispatch._

import scala.concurrent.{ExecutionContext, Future}

class CatsHttpApi(httpClient:Http, baseUri:Req)(implicit executor: ExecutionContext) extends CatsApi{
  override def catImage = {
    val req: Req = (baseUri / "images" / "get").addQueryParameter("format","xml")
    httpClient(req OK as.xml.Elem).either.map(either=>{
      Xor.fromEither(either).map(CatImage(_)).leftMap({
        case StatusCode(code) => ApiError(code)
        case t: Throwable => Error(t)
      })
    })
  }

  override def catCategories:Future[Xor[CatsApiError,List[CatCategory]]] = {
    val req: Req = (baseUri / "categories" / "list")
    httpClient(req OK as.xml.Elem).either.map(either=>{
      Xor.fromEither(either).map(CatCategory.fromXml(_)).leftMap({
        case StatusCode(code) => ApiError(code)
        case t: Throwable => Error(t)
      })
    })
  }
}

trait CatsApi {
  def catImage:Future[Xor[CatsApiError, CatImage]]
  def catCategories:Future[Xor[CatsApiError,List[CatCategory]]]
}
