package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class ImmutableCountingActor extends Actor with ActorLogging {

  def process( cnt: Int): PartialFunction[Any, Unit] = {
    case s: String =>
      log.info(s"Received msg $s - cnt = ${cnt+1}")
      context.become(process(cnt+1))
    case i: Int =>
      log.info(s"Received msg $i - cnt = ${cnt+1}")
      context.become(process(cnt+1))
  }

  override def receive: Receive = process(0)
}

object ImmutableState extends App {
  def props() = Props(classOf[ImmutableCountingActor])

  val actorSystem = ActorSystem("MyActorSystem")

  val actor: ActorRef = actorSystem.actorOf(
    ImmutableState.props(), name = "ImmutableCountingActor")

  (0 to 10).foreach(x => actor ! x)

  Thread.sleep(1000)

  actorSystem.terminate()

}


