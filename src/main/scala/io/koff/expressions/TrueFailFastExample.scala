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
 * Show how Expression behaves when an error occurs
 */
object TrueFailFastExample extends App {

  def shortFuture():Future[String] = {
    Future {
      Thread.sleep(3000)
      println("future #1 is completed after 3 sec")
      "future #1"
    }
  }

  def longFuture(): Future[String] = {
    Future {
      Thread.sleep(15000)
      println("future #2 is completed after 15 sec")
      "future #2"
    }
  }

  def failedFuture(): Future[String] = {
    Future {
      Thread.sleep(6000)
      println("future is failed after 6 sec")
      throw new IllegalStateException("future is failed")
    }
  }

  def expressionFailFast(): Unit = {
    println(" --- Test Expression --- ")
    val startTime = System.currentTimeMillis()
    val result = Expression[Future, String] {
      //same order as in the for-comprehension example
      val long = extract(longFuture())
      val short = extract(shortFuture())
      val failed = extract(failedFuture())
      short + " | " + long + " | " + failed
    }
    try {
      Await.result(result, 30 seconds)
    } catch {
      case err: Throwable => println("error: " + err.getMessage)
    }

    val duration = System.currentTimeMillis() - startTime
    println("duration: " + duration / 1000.0d)
    println(" --- --- --- --- ")
    println()
  }

  def forComprehensionFailFast(): Unit = {
    println(" --- Test For-Comprehension --- ")
    val startTime = System.currentTimeMillis()

    val longFut = longFuture()
    val shortFut = shortFuture()
    val failedFut = failedFuture()

    val result = for {
      long <- longFut
      short <- shortFut
      failed <- failedFut
    } yield {
      long + " | " + short + " | " + failed
    }

    try {
      Await.result(result, 30 seconds)
    } catch {
      case err: Throwable => println("error: " + err.getMessage)
    }

    val duration = System.currentTimeMillis() - startTime
    println("duration: " + duration / 1000.0d)
    println(" --- --- --- --- ")
    println()
  }

  expressionFailFast()

  //wait for long future
  Thread.sleep(15000)

  forComprehensionFailFast()

}
