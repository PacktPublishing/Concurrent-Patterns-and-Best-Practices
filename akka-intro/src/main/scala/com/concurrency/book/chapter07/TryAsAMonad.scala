package com.concurrency.book.chapter07

import scala.util.{Failure, Success, Try}

object TryAsAMonad extends App {
  def check1(x: Int) = Try {
    x match {
      case _ if x % 2 == 0 => x
      case _ => throw new RuntimeException("Number needs to be even")
    }
  }

  def check2(x: Int) = Try {
    x match {
      case _ if x < 1000 => x
      case _ => throw new RuntimeException("Number needs to be less than 1000")
    }
  }

  def check3(x: Int) = Try {
    x match {
      case _ if x > 500 => x
      case _ => throw new RuntimeException("Number needs to be greater than 500")
    }
  }

  val result = for {
    a <- check1(700)
    b <- check2(a)
    c <- check3(b)
  } yield "All checks ran just fine"

//  val result = check1(400).flatMap(a => check2(a).
//    flatMap(b => check3(b).map(c => "All checks ran just fine")))

  result match {
    case Success(s) => println(s)
    case Failure(e) => println(e)
  }


}
