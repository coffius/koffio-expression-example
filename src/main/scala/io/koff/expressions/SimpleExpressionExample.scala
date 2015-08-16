package io.koff.expressions

import com.github.jedesah.Expression
import com.github.jedesah.Expression._
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture.futureInstance
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
  def future1(): Future[String] = Future.successful("future1")
  /**
   * Async operation #2
   */
  def future2(): Future[String] = Future.successful("future2")
  /**
   * Result calculation
   */
  def resultCalc(str1: String, str2: String): String = str1 + " | " + str2
  /**
   * We need to use `extract(...)` method in order to get result from futures
   */
  def expression(): Future[String] = Expression[Future, String] {
    val result1 = extract(future1())
    val result2 = extract(future2())
    resultCalc(result1, result2)
  }

  /**
   * For-Comprehension analog for `expression()` method
   */
  def forComprehension(): Future[String] = {
    val fut1 = future1()
    val fut2 = future2()
    for {
      result1 <- fut1
      result2 <- fut2
    } yield {
      resultCalc(result1, result2)
    }
  }

  println("expression: " + Await.result(expression(), 30 seconds))
  println("for-comprehension: " + Await.result(forComprehension(), 30 seconds))

  /**
   * Example of usage of auto extraction
   */
  def autoExtract(): Future[String] = {
    import com.github.jedesah.Expression.auto.extract
    Expression[Future, String] {
      val result1 = future1()
      val result2 = future2()
      resultCalc(result1, result2)
    }
  }

  println("autoExtract: " + Await.result(autoExtract(), 30 seconds))
}
