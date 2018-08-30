package com.concurrency.book.chapter08

import scala.annotation.tailrec

object CountElems extends App {

  def size(l: List[Int]) = {

    @tailrec
    def countElems(list: List[Int], count: Int): Int = list match {
      case Nil     => count
      case x :: xs => countElems(xs, count+1)
    }

    countElems(l, 0)
  }

  val list = List(1, 2, 3, 4, 5)

  println(size(list))
}
