package com.cats

sealed abstract class CatsError

case class ApiError(statusCode: Int) extends CatsError
case class Error(exception: Throwable) extends CatsError
case class JsonParseError(jsonRaw:String) extends CatsError
case class RepositoryHttpError(statusCode: Int) extends CatsError
case class RepositoryWriteError(exception: Throwable) extends CatsError