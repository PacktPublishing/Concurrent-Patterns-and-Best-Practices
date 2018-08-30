package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import scala.concurrent.Future
import akka.pattern.{ask, pipe}
import scala.concurrent.ExecutionContext.Implicits.global

class PipeToActor extends Actor with ActorLogging{
  override def receive: Receive = process(List.empty)

  def process(list: List[Int]): Receive = {
    case x : Int => {
      val result = checkIt(x).map((x, _))
      pipe(result) to self
    }
    case (x: Int, b: Boolean) => log.info(s"${x} is ${b}")
  }

  def checkIt(x: Int): Future[Boolean] = Future {
    Thread.sleep(1000)
    x % 2 == 0
  }
}

object PipeToActor extends App {
  val actorSystem = ActorSystem("MyActorSystem")

  def props( ) = Props(classOf[PipeToActor])

  val actor = actorSystem.actorOf(PipeToActor.props(), "actor1")

  (1 to 5).foreach(x => actor ! x)

  Thread.sleep(5000)

  actorSystem.terminate()
}
