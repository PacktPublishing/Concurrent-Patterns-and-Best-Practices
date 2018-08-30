package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

class CountMessagesActor( var cnt: Integer ) extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case s: String =>
      cnt += 1
      log.info(s"Received msg $s - cnt = ${cnt}")
    case i: Int =>
      cnt += 1
      log.info(s"Received msg $i - cnt = ${cnt}")
  }

}

object CountMessagesActor extends App {
  def props( cnt: Int ) = Props(new HandlesOnlyFiveMessages())

  val actorSystem = ActorSystem("MyActorSystem")

  val actor = actorSystem.actorOf(CountMessagesActor.props(1), name = "CountMessagesActor")

  actor ! "Hi"
  actor ! 34

  case class Msg( msgNo: Int )

  actor ! Msg(3)

  actor ! 35

  actorSystem.terminate()
}
