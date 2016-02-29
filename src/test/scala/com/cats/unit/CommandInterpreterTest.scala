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
  describe("command interpreter") {
    it("call api to get a random cat image when receive 'file' command") {
      val catApi = mock[CatsApi]
      val res:Xor[CatsApiError, CatImage] = Xor.right(CatImage("url1","cat"))
      when(catApi.catImage).thenReturn(Future{res})
      val commandInterpreter = new CommandInterpreter(catApi)
      val executor = commandInterpreter.runCommand(Array("file"))
      executor.run({value=> value should be(res)})
    }

    it("call api to get a list of cat's categories when receive 'categories' command") {
      val catApi = mock[CatsApi]
      val res:Xor[CatsApiError, List[CatCategory]] = Xor.right(List(CatCategory(1,"test")))
      when(catApi.catCategories).thenReturn(Future{res})
      val commandInterpreter = new CommandInterpreter(catApi)
      val executor = commandInterpreter.runCommand(Array("categories"))
      executor.run({value=> value should be(res)})
    }

    it("call api to get a cat's fact when receive 'fact' command") {
      val catApi = mock[CatsApi]
      val res:Xor[CatsApiError, String] = Xor.right("cat fact")
      when(catApi.catFact).thenReturn(Future{res})
      val commandInterpreter = new CommandInterpreter(catApi)
      val executor = commandInterpreter.runCommand(Array("fact"))
      executor.run({value=> value should be(res)})
    }
  }
}
