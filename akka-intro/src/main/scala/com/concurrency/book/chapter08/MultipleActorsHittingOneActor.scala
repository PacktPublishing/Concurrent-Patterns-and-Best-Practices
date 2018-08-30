package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class Make5From1Actor( actor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case s =>
      log.info(s"Received msg $s")
      (0 to 4).foreach(p => actor ! s)
  }
}

class Make3From1Actor( actor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case s =>
      log.info(s"Received msg $s")
      (0 to 2).foreach(p => actor ! s)
  }
}

object MultipleActorsHittingOneActor extends App {
  val actorSystem = ActorSystem("MyActorSystem")

  val actor = actorSystem.actorOf(CountMessagesActor.props(0), name = "CountMessagesActor")

  def props5From1() = Props(new Make5From1Actor(actor))
  def props3From1() = Props(new Make3From1Actor(actor))

  val actor1 = actorSystem.actorOf(props5From1(), name = "Actor5From1")
  val actor2 = actorSystem.actorOf(props3From1(), name = "Actor3From1")

  actor1 ! 34
  actor1 ! "hi"
  actor2 ! 34
  actor2 ! "hi"

  Thread.sleep(1000)
  actorSystem.terminate()
}
