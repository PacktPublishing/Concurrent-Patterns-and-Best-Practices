package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class HandlesOnlyFiveMessages extends Actor with ActorLogging {

  var cnt = 0

  def stopProcessing: Receive = PartialFunction.empty

  override def receive: Receive = {
    case i: Int =>
      cnt += 1
      log.info(s"Received msg $i - cnt = ${cnt}")
      if (cnt == 5) context.become(stopProcessing)
  }

}

object HandlesOnlyFiveMessages extends App {
  def props() = Props(classOf[HandlesOnlyFiveMessages])

  val actorSystem = ActorSystem("MyActorSystem")

  val actor: ActorRef = actorSystem.actorOf(
    HandlesOnlyFiveMessages.props(), name = "HandlesOnlyFiveMessagesActor")

  (0 to 10).foreach(x => actor ! x)

  Thread.sleep(1000)

  actorSystem.terminate()

}


