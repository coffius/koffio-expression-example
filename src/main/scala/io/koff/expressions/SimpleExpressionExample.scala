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

/**
 * A very simple example which shows how to use Expression object
 */
object SimpleExpressionExample extends App {

  /**
   * Async operation #1
   */
  def future1(): Future[String] = {
    Future.successful("future1")
  }

  /**
   * Async operation #2
   */
  def future2(): Future[String] = {
    Future.successful("future2")
  }

  /**
   * Result calculation
   */
  def resultCalc(str1: String, str2: String): String = {
    str1 + " | " + str2
  }

  /**
   * We need to use `extract(...)` method in order to get result from futures
   */
  val result = Expression[Future, String] {
    val result1 = extract(future1())
    val result2 = extract(future2())
    resultCalc(result1, result2)
  }

  val forComprehensionAnalog = for {
    result1 <- future1()
    result2 <- future2()
  } yield {
      resultCalc(result1, result2)
  }

  println("expression: " + Await.result(result, 30 seconds))
  println("for-comprehension: " + Await.result(forComprehensionAnalog, 30 seconds))
}
