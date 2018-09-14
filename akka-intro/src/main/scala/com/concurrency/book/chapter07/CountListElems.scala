package com.concurrency.book.chapter07

import scala.annotation.tailrec

object CountListElems extends App {

  def count(l: List[Int]): Int = {

    @tailrec
    def countElems(list: List[Int], count: Int): Int = list match {
      case Nil => count
      case x :: xs => countElems(xs, count + 1)
    }

    countElems(l, 0)
  }

  println(count(List(1, 2, 3, 4, 5)))
  println(count(( 1 to 100000 ).toList))
}
