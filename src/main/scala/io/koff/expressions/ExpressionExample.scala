package io.koff.expressions

import com.github.jedesah.Expression
import com.github.jedesah.Expression._
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture.futureInstance
import com.github.jedesah.Expression.auto.extract
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ExpressionExample extends App {

  def future1(): Future[String] = {
    Future.successful("future1")
  }

  def future2(): Future[String] = {
    Future.successful("future2")
  }

  def resultCalc(str1: String, str2: String): String = {
    str1 + " | " + str2
  }

  val result = Expression[Future, String] {
    val result1 = extract(future1())
    val result2 = extract(future2())
    resultCalc(result1, result2)
  }

  println(Await.result(result, 30 seconds))
}
