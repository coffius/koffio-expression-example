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
 * Show how Expression can be used wit if statements
 */
object FutureIfExample extends App {
  def trueFuture():Future[Boolean] = {
    Future {
      Thread.sleep(3000)
      println("true future is completed after 3 sec")
      true
    }
  }

  def normalFuture(): Future[String] = {
    Future {
      Thread.sleep(10000)
      println("normal future is completed after 10 sec")
      "normal future"
    }
  }

  def longFuture(): Future[String] = {
    Future {
      Thread.sleep(15000)
      println("long future is completed after 15 sec")
      "long future"
    }
  }

  def failedFuture(): Future[String] = {
    Future {
      Thread.sleep(6000)
      println("future is failed after 6 sec")
      throw new IllegalStateException("future is failed")
    }
  }

  /**
   * Result calculation
   */
  def resultCalc(str1: String, str2: String): String = str1 + " | " + str2

  /**
   * This method shows how `Expression` works with if-statements.
   * In this example `failedFuture()` from B-branch will never be called.
   */
  def expressionIf(): Future[String] = {
    import com.github.jedesah.Expression.auto.extract
    Expression[Future, String] {
      if(extract(trueFuture())) {
        //A-branch
        resultCalc(normalFuture(), longFuture())
      } else {
        //B-branch
        resultCalc("default", failedFuture())
      }
    }
  }

  /**
   * An analog for `expressionIf()` without waiting all the possible futures
   */
  def forComprehensionIf(): Future[String] = {
    (for {
      condition <- trueFuture()
    } yield {
      if(condition) {
        for{
          normal <- normalFuture()
          long <- longFuture()
        } yield {
          resultCalc(normal, long)
        }
      } else {
        for{
          failed <- failedFuture()
        } yield {
          resultCalc("default", failed)
        }
      }
    }).flatMap(identity)
  }

  val startTime = System.currentTimeMillis()
  private val expressionIfResult = Await.result(expressionIf(), 30 seconds)
  val duration = System.currentTimeMillis() - startTime
  println(s"expressionIf[duration: ${duration / 1000.0d}]: " + expressionIfResult)
}
