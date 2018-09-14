  package com.concurrency.book.chapter07

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

  object AsynchronousComputation extends App {

    def longRunningMethod( i: Int ) = Future{
      Thread.sleep(i * 1000)
      i
    }

    val start = System.currentTimeMillis()

    val f1 = longRunningMethod(2)
    val f2 = longRunningMethod(2)
    val f3 = longRunningMethod(2)

    val f4: Future[List[Int]] = Future.sequence(List(f1, f2, f3))

    val result = Await.result( f4, 4 second)

    println(result.sum)

    val stop = System.currentTimeMillis()
    println(s"Time taken ${stop - start} ms")

    Thread.sleep(4000)
  }
