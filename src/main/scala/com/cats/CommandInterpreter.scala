package com.cats

import scala.concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.concurrent.duration._

class CommandInterpreter(catApi:CatsApi, catRepo:CatImageRepository) {

  def runCommand(parameters:Array[String]) = {
    parameters match {
      case Array("file") => new CommandExecutor(catApi.catImage.flatMap(catRepo.saveImage(_)))
      case Array("categories") => new CommandExecutor(catApi.catCategories)
      case Array("fact") => new CommandExecutor(catApi.catFact)
      case _ => throw new RuntimeException("Command not support please check README file")
    }
  }
}


class CommandExecutor[T](future:Future[T]) {
  def runAndWait: T = {
    Await.result(future, 5 second)
  }
}
