package com.concurrency.book.chapter07

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, blocking}

object BlockingFutures extends App {
  val start = System.currentTimeMillis()

  val listOfFuts = List.fill(16)(Future {
    blocking {
      Thread.sleep(2000)
      println("-----")
    }
  })

  listOfFuts.map(future => Await.ready(future, Duration.Inf))

  val stop = System.currentTimeMillis()
  println(s"Time taken ${stop - start} ms")
  println(s"Total cores = ${Runtime.getRuntime.availableProcessors}")

}
