package com.concurrency.book.chapter07

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureWithDefaultContext extends App {

  Future {
    Thread.sleep(2000)
    println("Hello, world!")
  }
  println("How are things?")
}
