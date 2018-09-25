  package com.concurrency.book.chapter07

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.{Await, Future}
  import scala.concurrent.duration._

  object FutureWithForComprehension extends App {

    def longRunningMethod( i: Int ) = Future{
      Thread.sleep(i * 1000)
      i
    }

    val start = System.currentTimeMillis()

    val f1 = longRunningMethod(2)
    val f2 = longRunningMethod(2)
    val f3 = longRunningMethod(2)

    val f4: Future[Int] = for {
      x <- f1
      y <- f2
      z <- f3
    } yield ( x + y + z )

    // Don't use this version...
//    val f4: Future[Int] = for {
//      x <- longRunningMethod(2)
//      y <- longRunningMethod(2)
//      z <- longRunningMethod(2)
//    } yield ( x + y + z )

    // Or this
//    val f4 = longRunningMethod(2).flatMap(x => longRunningMethod(2).
//      flatMap(y => longRunningMethod(2).map(z => x + y + z)))

    val result = Await.result( f4, 4 second)

    println(result)

    val stop = System.currentTimeMillis()
    println(s"Time taken ${stop - start} ms")

    Thread.sleep(4000)
  }
