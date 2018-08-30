package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ActorTestAsk extends Actor with ActorLogging {
  override def receive: Receive = {
    case s: String => sender ! s.toUpperCase
    case i: Int => sender ! (i + 1)
  }
}

object ActorTestAsk extends App {
  def props() = Props(classOf[ActorTestAsk])
  val actorSystem = ActorSystem("MyActorSystem")

  val actor = actorSystem.actorOf(ActorTestAsk.props(), name = "actor1")
  implicit val timeout = Timeout(5 seconds)

  val future: Future[Any] = actor ? "hello"

  future foreach println

  Thread.sleep(2000)
  actorSystem.terminate()
}
