package com.concurrency.book.chapter08

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Actor1 {
  def props(workActor: ActorRef) = Props(new TellActor1(workActor))
}

class Actor1(workActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case s: String => {
      implicit val timeout = Timeout(20 seconds)

      val future = workActor ? s.toUpperCase
      future onComplete {
        case Success(s) => log.info(s"Got '${s}' back")
        case Failure(e) => log.info(s"Error'${e}'")
      }
    }
  }
}

class Actor2 extends Actor with ActorLogging {
  override def receive: Receive = {
    case s: String => {
      val senderRef = sender() //sender ref needed for closure
      Future {
        val r = new scala.util.Random
        val delay = r.nextInt(500)+10
        Thread.sleep(delay)
        s.toUpperCase
      } foreach { reply =>
        senderRef ! reply
      }
    }
  }
}

object ActorToActorAsk extends App {
  val actorSystem = ActorSystem("MyActorSystem")

  val workactor = actorSystem.actorOf(Props[TellActor2], name = "workactor")

  val actor = actorSystem.actorOf(TellActor1.props(workactor), name = s"actor")

  val actorNames = (0 to 50).map(x => s"actor${x}")
  val actors = actorNames.map(actorName => actorSystem.actorOf(TellActor1.props(workactor), name = actorName))

  (actorNames zip actors) foreach { case (name, actor) => actor ! name }

}

object ClosesOver extends App {
  class Foo {
    def m1( fun: ( Int ) => Unit, id: Int ) {
      fun(id)
    }
  }

  var x = 1

  def addUp( num: Int ) = println(num + x)

  val foo = new Foo
  foo.m1(addUp, 1)

  x = 2

  foo.m1(addUp, 1)
}

