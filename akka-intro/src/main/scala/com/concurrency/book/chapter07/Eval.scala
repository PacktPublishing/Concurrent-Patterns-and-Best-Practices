package com.concurrency.book.chapter07

object Eval extends App {
  def eagerEval( b: Boolean, ifTrue: Unit, ifFalse: Unit ) =
    if ( b )
      ifTrue
    else
      ifFalse

  def delayedEval( b: Boolean, ifTrue: => Unit, ifFalse: => Unit ) =
    if ( b )
      ifTrue
    else
      ifFalse

  eagerEval(9 == 9, println("9 == 9 is true"), println("9 == 9 is false"))
  println("---------")
  delayedEval(9 == 9, println("9 == 9 is true"), println("9 == 9 is false"))
  delayedEval(9 != 9, println("9 == 9 is true"), println("9 == 9 is false"))
}
