package com.concurrency.book.chapter07

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}

object FutWithMyContext extends App {
  implicit val execContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool)

  Future {
    Thread.sleep(2000)
    println("Hello, world!")
  }
  println("How are things?")
}
