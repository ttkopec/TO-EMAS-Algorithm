package pl.edu.agh.to.core

import akka.actor.{Actor, ActorRef, Terminated}

import scala.collection.mutable.ArrayBuffer

class Reaper extends Actor {

  import Reaper._

  val watched = ArrayBuffer.empty[ActorRef]

  def allTerminated(): Unit =
    context.system.terminate()

  override def receive: Receive = {
    case WatchMe(ref) =>
      context.watch(ref)
      watched += ref

    case Terminated(ref) =>
      watched -= ref
      if (watched.isEmpty) allTerminated()
  }
}

object Reaper {

  case class WatchMe(ref: ActorRef)


}