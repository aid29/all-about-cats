package com.cats

import java.io.{BufferedOutputStream, FileOutputStream}
import java.util.UUID

import cats.data.Xor
import dispatch._

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.util.Try

trait CatImageRepository {
  def saveImage(catImage:Xor[CatsError, CatImage]):Future[Xor[CatsError,CatImage]]
}

class ConcretCatImageRepository(httpClient:Http, fileNameGenerator: FileNameGenerator) extends CatImageRepository {
  override def saveImage(maybeCatImage:Xor[CatsError, CatImage]) = {
    maybeCatImage match {
      case Xor.Left(err:CatsError) => Future.successful(Xor.left(err))
      case Xor.Right(catImage) => {
        val either: Future[Either[Throwable, Array[Byte]]] = httpClient(url(catImage.url) OK as.Bytes).either

        def writeFile(byteArray:Array[Byte]) = {
          val tryToWrite = Try({
            val fileName: String = fileNameGenerator.generate()
            val bos = new BufferedOutputStream(new FileOutputStream(fileName))
            Stream.continually(bos.write(byteArray))
            bos.close()
            catImage.copy(filePath = Some(fileName))
          })
          Xor.fromTry(tryToWrite).leftMap({
            case t:Throwable => RepositoryWriteError(t)
          })
        }

        httpClient(url(catImage.url) OK as.Bytes).either.map(either=>{
          Xor.fromEither(either).flatMap(writeFile(_)).leftMap({
            case StatusCode(code) => RepositoryHttpError(code)
            case t: Throwable => Error(t)
          })
        })
      }
    }
  }
}

trait FileNameGenerator {
  def generate():String
}

class UniqueFileNameGenerator extends FileNameGenerator {
  override def generate(): String = UUID.randomUUID().toString
}
