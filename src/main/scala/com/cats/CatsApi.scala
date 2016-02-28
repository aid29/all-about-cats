package com.cats

import cats.data.Xor
import dispatch._

import scala.concurrent.{ExecutionContext, Future}

class CatsHttpApi(httpClient:Http, baseUri:Req)(implicit executor: ExecutionContext) extends CatsApi{
  override def catImage = {
    val req: Req = (baseUri / "images" / "get").addQueryParameter("format","xml")
    httpClient(req OK as.xml.Elem).either.map(either=>{
      Xor.fromEither(either).map(CatImage(_))
    })
  }
}

trait CatsApi {
  def catImage:Future[Xor[Throwable, CatImage]]
}
