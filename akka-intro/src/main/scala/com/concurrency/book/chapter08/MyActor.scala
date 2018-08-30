package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class MyActor extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case s: String => println(s"<${s}>")
    case i: Int => println(i+1)
  }

}

object MyActor extends App {
  def props() = Props(new MyActor)

  val actorSystem = ActorSystem("MyActorSystem")

  val actor: ActorRef = actorSystem.actorOf(MyActor.props(), name = "MyActor")

  actor ! "Hi"
  actor ! 34

  case class Msg( msgNo: Int)

  actor ! Msg(3)

  actor ! 35

  actorSystem.terminate()
}
