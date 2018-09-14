package com.concurrency.book.chapter07

object SynchronousExecution extends App {

  def longRunningMethod( i: Int ) = {
    Thread.sleep(i * 1000)
    i
  }

  val start = System.currentTimeMillis()

  val result = longRunningMethod(2) + longRunningMethod(2) + longRunningMethod(2)

  val stop = System.currentTimeMillis()

  println(s"Time taken ${stop - start} ms")

  println(result)

  Thread.sleep(7000)
}
