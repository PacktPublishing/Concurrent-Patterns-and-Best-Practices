package com.concurrency.book.chapter08

import akka.actor.ActorSystem
import com.concurrency.book.chapter08.MyActor.actorSystem

object ThousandsOfActors extends App {
  val actorSystem = ActorSystem("MyActorSystem")

  (1 to 10000).
    map(k => k -> actorSystem.actorOf(MyActor.props(), name = s"MyActor${k}")).
    foreach { case (k, actor) => actor ! k }

  Thread.sleep(14000)
  actorSystem.terminate()
}
