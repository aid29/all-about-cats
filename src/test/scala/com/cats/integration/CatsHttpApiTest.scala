package com.cats.integration

import cats.data.Xor
import com.cats._
import com.cats.fake.FakeCatApi
import dispatch.{url, Http}
import org.scalatest.{BeforeAndAfterAll, FunSpec}
import org.scalatest.Matchers._
import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

class CatsHttpApiTest extends FunSpec with BeforeAndAfterAll{

  val httpClient = Http()
  val fakeServer = new FakeCatApi(8099)
  val catsHttpApi = new CatsHttpApi(httpClient, url("http://localhost:8099"), url("http://localhost:8099"))

  describe("get cat image endpoint") {
    it("can return a random cat image url") {
      val catImageXml = <response>
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
      fakeServer.withCatImageXml(catImageXml) {
        val returnCatImage = Await.result(catsHttpApi.catImage, 5 seconds)
        returnCatImage should be(Xor.Right(CatImage("http://24.media.tumblr.com/tumblr_m4j2e7Fd7M1qejbiro1_1280.jpg", "dqd")))
      }
    }

    it("return ApiError with http statuts code if received http error") {
      fakeServer.failedOnCatImage(500) {
        Await.result(catsHttpApi.catImage, 5 seconds) should be(Xor.left(ApiError(500)))
      }
    }
  }

  describe("get cat categories list endpoint") {
    it("return cat's categories list ordering by the name") {
      val catCategoriesXml = <response>
        <data>
          <categories>
            <category>
              <id>1</id>
              <name>hats</name>
            </category>
            <category>
              <id>2</id>
              <name>space</name>
            </category>
          </categories>
        </data>
      </response>
      fakeServer.withCatCategoriesXml(catCategoriesXml) {
        val categories = Await.result(catsHttpApi.catCategories, 5 seconds).toOption.get
        categories.length should be(2)
        categories should contain(CatCategory(1,"hats"))
        categories should contain(CatCategory(2,"space"))
      }

    }

    it("return ApiError with http status code if received http error") {
      fakeServer.failedOnCatCategories(500) {
        Await.result(catsHttpApi.catCategories, 5 seconds) should be(Xor.left(ApiError(500)))
      }
    }
  }

  describe("get cat fact endpoint") {
    it("return a cat fact") {
      val catFactJson =
        """
          |{
          |    "facts": [
          |        "A cat cannot see directly under its nose."
          |    ],
          |    "success": "true"
          |}
        """.stripMargin

      fakeServer.withCatFactJson(catFactJson) {
        val catFact = Await.result(catsHttpApi.catFact, 5 seconds).toOption.get should be("A cat cannot see directly under its nose.")
      }
    }

    it("return ApiError with http status code if received http error") {
      fakeServer.failedOnCatFact(500) {
        Await.result(catsHttpApi.catFact, 5 seconds) should be(Xor.left(ApiError(500)))
      }
    }
  }



//  it("test") {
//    val repo = new ConcretCatImageRepository(httpClient, new UniqueFileNameGenerator)
//    Await.result(repo.saveImage(Xor.right(CatImage("http://25.media.tumblr.com/tumblr_lkryzaiBXX1qbe5pxo1_1280.jpg","xxd"))), 5 seconds)
//  }
  override protected def beforeAll(): Unit = fakeServer.start()

  override protected def afterAll(): Unit = fakeServer.stop()
}
