package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class UnhandledMsgActor extends Actor with ActorLogging {
  override def receive: Receive = PartialFunction.empty

  override def unhandled( message: Any ) = message match {
    case msg: Int => log.info(s"I got ${msg} - don't know what to do with it?")
    case msg => super.unhandled(msg)
  }
}

object UnhandledMsgActor extends App {
  def props() = Props(new UnhandledMsgActor)

  val actorSystem = ActorSystem("MyActorSystem")

  val actor: ActorRef = actorSystem.actorOf(UnhandledMsgActor.props(), name = "UnhandledMsgActor")

  actor ! "Hi"
  actor ! 12
  actor ! 3.4

  Thread.sleep(1000)

  actorSystem.terminate()
}
