package com.cats.integration

import cats.data.Xor
import com.cats.{ApiError, CatImage, CatsHttpApi}
import com.cats.fake.FakeCatApi
import dispatch.{url, Http}
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class CatsHttpApiTest extends FunSpec{

  val httpClient = Http()
  val fakeServer = new FakeCatApi(8099)
  val catsHttpApi = new CatsHttpApi(httpClient, url("http://localhost:8099"))

  describe("cats http api") {
    it("return a random cat image") {
      val xmlRes = <response>
        <data>
          <images>
            <image>
              <url>http://24.media.tumblr.com/tumblr_m4j2e7Fd7M1qejbiro1_1280.jpg</url>
              <id>dqd</id>
              <source_url>http://thecatapi.com/?id=dqd</source_url>
            </image>
          </images>
        </data>
      </response>
      fakeServer.withCatImageXml(xmlRes) {
        val returnCatImage = Await.result(catsHttpApi.catImage, 5 seconds)
        returnCatImage should be(Xor.Right(CatImage("http://24.media.tumblr.com/tumblr_m4j2e7Fd7M1qejbiro1_1280.jpg","dqd")))
      }
    }

    it("return ApiError if received http error") {
      fakeServer.failedOn(500) {
        Await.result(catsHttpApi.catImage, 5 seconds) should be(Xor.left(ApiError(500)))
      }
    }
  }
}
