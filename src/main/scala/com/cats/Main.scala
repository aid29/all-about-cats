package com.cats

import cats.data.Xor
import dispatch.{url, Http}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Main {

  def main(args: Array[String]): Unit = {
    val httpTimeout = sys.env.get("HTTP_TIMEOUT").getOrElse(5000)
    val catApiBaseUrl = sys.env.get("CAT_API_URL").getOrElse(throw new RuntimeException("Not cat api url find in env"))
    val catFactApiBaseUrl = sys.env.get("CAT_FACT_API_URL").getOrElse(throw new RuntimeException("Not cat fact api url find in env"))
    val http = Http.configure(_.setAllowPoolingConnection(true).setConnectionTimeoutInMs(5000).setRequestTimeoutInMs(5000))
    val catApi = new CatsHttpApi(http, url(catApiBaseUrl), url(catFactApiBaseUrl))
    val catRepo = new ConcretCatImageRepository(http, new UniqueFileNameGenerator)
    val ci = new CommandInterpreter(catApi, catRepo)

    ci.runCommand(args).runAndWait match {
      case Xor.Right(image:CatImage) => println("Downloaded cat image form %s and saved it into file %s".format(image.url, image.filePath.get))
      case Xor.Right(list:List[CatCategory]) => list.map(cc=>println(cc.name))
      case Xor.Right(fact:String) => println(fact)
      case Xor.Left(e:ApiError) => println("Command failed due to API http status code " + e.statusCode)
      case Xor.Left(e:Error) => println("Command failed due to exception " + e.exception.getMessage)
      case Xor.Left(e:JsonParseError) => println("Command failed due to json response: " + e.jsonRaw)
      case Xor.Left(e:RepositoryHttpError) => println("Command failed due to download the image with status code " + e.statusCode)
      case Xor.Left(e:RepositoryWriteError) => println("Command failed due to write the image to the file with " + e.exception.getMessage)
    }
  }

}
