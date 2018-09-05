package com.concurrency.book.chapter08

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorKilledException, ActorLogging, ActorRef, ActorSystem, Identify, OneForOneStrategy, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask
import com.concurrency.book.chapter08.Child.BadMessageException

import scala.concurrent.{Await, Future}

class Child extends Actor with ActorLogging {
  import Child._
  def receive = {
    case i: Int => log.info(s"${i + 1}")
    case _ => throw BadMessageException("Anything other than Int messages is not supported")
  }
}

object Child {
  def props( ) = Props(classOf[Child])

  case class BadMessageException(errStr: String) extends RuntimeException
}

class Supervisor extends Actor with ActorLogging {
  val child = context.actorOf(Child.props(), "child")

  def receive = {
    case _: Any => sender ! child
  }

  override val supervisorStrategy =
    OneForOneStrategy(){
      case e: BadMessageException => Restart
      case _ => Escalate
    }
}

object Supervisor extends App {
  val actorSystem = ActorSystem("MyActorSystem")
  implicit val timeout = Timeout(5 seconds)

  def props( ) = Props(classOf[Supervisor])

  val actor = actorSystem.actorOf(Supervisor.props(), "parent")

  val future = actor ? "hi"

  val child = Await.result(future, 5 seconds).asInstanceOf[ActorRef]

  child ! 1
  child ! 2

  child ! "hi"

  child ! 3

  Thread.sleep(2000)

  actorSystem.terminate()
}



