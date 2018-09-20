package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TellActor1 {
  def props(workActor: ActorRef) = Props(new TellActor1(workActor))
  case class Result(s: String)
}

class TellActor1( workActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case s: String        => workActor ! s
    case TellActor1.Result(s) => log.info(s"Got '${s}' back")
  }
}

class TellActor2 extends Actor with ActorLogging {
  override def receive: Receive = {
    case s: String => {
      val senderRef = sender() //sender ref needed for closure
      Future {
        Thread.sleep(1000)
        TellActor1.Result(s.toUpperCase)
      } foreach { reply =>
        senderRef ! reply
      }
    }
  }
}

object ActorToActorTell extends App {
  val actorSystem = ActorSystem("MyActorSystem")

  val workactor = actorSystem.actorOf(Props[TellActor2], name = "workactor")

  val actor = actorSystem.actorOf(TellActor1.props(workactor), name = s"actor")

  val actorNames = (0 to 50).map(x => s"actor${x}")
  val actors = actorNames.map(actorName => actorSystem.actorOf(TellActor1.props(workactor), name = actorName))

  (actorNames zip actors) foreach { case (name, actor) => actor ! name }

  Thread.sleep(14000)

  actorSystem.terminate()

}


