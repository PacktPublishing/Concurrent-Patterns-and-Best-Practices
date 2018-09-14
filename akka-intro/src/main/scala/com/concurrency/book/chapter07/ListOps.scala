package com.concurrency.book.chapter07

object ListOps extends App {

  def prepend( elem: Int, list: List[Int] ) = list match {
    case Nil => List(elem)
    case _ => elem :: list
  }

  def append( elem: Int, list: List[Int] ): List[Int] = list match {
    case Nil => List(elem)
    case x :: xs => x :: append(elem, xs)
  }

  val list = List(1, 2, 3)
  println(prepend(0, list))
  println(append(4, list))

}
