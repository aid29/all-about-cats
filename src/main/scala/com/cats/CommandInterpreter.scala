package com.cats

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class CommandInterpreter(catApi:CatsApi) {

  def runCommand(parameters:Array[String]) = {
    parameters match {
      case Array("file") => new CommandExecutor(catApi.catImage)
      case Array("categories") => new CommandExecutor(catApi.catCategories)
      case Array("fact") => new CommandExecutor(catApi.catFact)
      case _ => throw new RuntimeException("Command not support please check README file")
    }
  }
}


class CommandExecutor[T](future:Future[T]) {
  def run(process: T => Unit): Unit = {
    future onSuccess {
      case value:T => process(value)

    }

    future onFailure {
      case t => println("Execution of command failed " + t.getMessage)
    }
  }
}
