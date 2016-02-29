package com.cats.unit

import cats.data.Xor
import com.cats._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar.mock
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

class CommandInterpreterTest extends FunSpec {

  it("call api to get a random cat image and call repository to save the image to the file when receive 'file' command") {
    val catApi = mock[CatsApi]
    val catRepo = mock[CatImageRepository]

    val apiRes: Xor[CatsError, CatImage] = Xor.right(CatImage("url1", "cat", None))
    val repoRes: Xor[CatsError, CatImage] = Xor.right(CatImage("url1", "cat", Some("xxx")))

    when(catApi.catImage).thenReturn(Future {
      apiRes
    })
    when(catRepo.saveImage(Xor.right(CatImage("url1", "cat", None)))).thenReturn(Future {
      repoRes
    })

    val commandInterpreter = new CommandInterpreter(catApi, catRepo)
    val executor = commandInterpreter.runCommand(Array("file"))
    executor.runAndWait should be(repoRes)
  }

  it("call api to get a list of cat's categories when receive 'categories' command") {
    val catApi = mock[CatsApi]
    val catRepo = mock[CatImageRepository]
    val res: Xor[CatsError, List[CatCategory]] = Xor.right(List(CatCategory(1, "test")))

    when(catApi.catCategories).thenReturn(Future {
      res
    })

    val commandInterpreter = new CommandInterpreter(catApi, catRepo)
    val executor = commandInterpreter.runCommand(Array("categories"))
    executor.runAndWait should be(res)
  }

  it("call api to get a cat's fact when receive 'fact' command") {
    val catApi = mock[CatsApi]
    val catRepo = mock[CatImageRepository]
    val res: Xor[CatsError, String] = Xor.right("cat fact")

    when(catApi.catFact).thenReturn(Future {
      res
    })

    val commandInterpreter = new CommandInterpreter(catApi, catRepo)
    val executor = commandInterpreter.runCommand(Array("fact"))
    executor.runAndWait should be(res)
  }
}
