package pl.edu.agh.to.core

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}
import pl.edu.agh.to.agent.Agent
import pl.edu.agh.to.core.Island.{Summary, Tick}
import pl.edu.agh.to.operators.Operator

import scala.collection.mutable.{Set => MSet}

class Island(operator: Operator) extends Actor {

  private val agents: MSet[Agent] = MSet.empty

  override def receive: Receive = {
    case Summary =>
      sender() ! agents.maxBy(_.getFitness)
    case Tick =>


  }

  def mainAction(): Unit = {
    val newGeneration = agents.toSeq.grouped(2).collect {
      case Seq(a, b) =>
        operator.copulate(a, b)
    }
    agents.retain(operator.selection)
    agents ++= newGeneration
  }
}

object Island {

  case class Migrate(isle: ActorRef)

  case object Stop

  case class AgentMessage(agent: Agent)

  case object Tick

  case object Summary

}
