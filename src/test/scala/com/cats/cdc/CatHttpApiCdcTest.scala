package com.cats.cdc

import com.cats.CatsJsonHelper
import dispatch._
import org.scalatest.FunSpec
import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import org.scalatest.Matchers._


class CatHttpApiCdcTest extends FunSpec{

  val catApiBaseUrl = sys.env.get("CAT_API_URL").getOrElse(throw new RuntimeException("Not cat api url find in env"))
  val catFactApiBaseUrl = sys.env.get("CAT_FACT_API_URL").getOrElse(throw new RuntimeException("Not cat fact api url find in env"))
  val http = Http.configure(_.setAllowPoolingConnection(true).setConnectionTimeoutInMs(5000).setRequestTimeoutInMs(5000))

  describe("the 3rd part API") {
    it("provide the cat fact endpoint") {
      val req: Req = (url(catFactApiBaseUrl) / "facts")
      val res: Future[String] = http(req OK as.String)
      CatsJsonHelper.extractFact(Await.result(res, 5 seconds)).toOption.isDefined should be(true)
    }
  }

}
