package com.cats

sealed abstract class CatsApiError

case class ApiError(statusCode: Int) extends CatsApiError
case class Error(exception: Throwable) extends CatsApiError