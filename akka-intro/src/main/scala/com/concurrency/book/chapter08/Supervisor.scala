package com.concurrency.book.chapter08

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorKilledException, ActorLogging, ActorRef, ActorSystem, OneForOneStrategy, Props}

class Child extends Actor with ActorLogging {
  def receive = {
    case i: Int => log.info(s"${i + 1}")
    case _ => throw new RuntimeException
  }
}

object Child {
  def props( ) = Props(classOf[Child])
}

class Supervisor extends Actor {
  val child = context.actorOf(Child.props(), "child")

  def receive = PartialFunction.empty

  override val supervisorStrategy =
    OneForOneStrategy(){
        case ex: ActorKilledException => Restart
      case _ => Escalate
    }
}

object Supervisor extends App {
  val actorSystem = ActorSystem("MyActorSystem")

  def props( ) = Props(classOf[Supervisor])

  val actor: ActorRef = actorSystem.actorOf(Supervisor.props(), "parent")

  actorSystem.actorSelection("/user/parent/child") ! 1
  actorSystem.actorSelection("/user/parent/child") ! 2
  actorSystem.actorSelection("/user/parent/child") ! "Hi"
  Thread.sleep(2000)

  actorSystem.actorSelection("/user/parent/child") ! 3

  Thread.sleep(2000)
  actorSystem.terminate()
}



