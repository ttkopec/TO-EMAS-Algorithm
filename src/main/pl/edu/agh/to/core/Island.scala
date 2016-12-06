package pl.edu.agh.to.core

import java.util.concurrent.ExecutionException

import akka.actor.{Actor, ActorRef, Props}
import pl.edu.agh.to.agent.Agent
import pl.edu.agh.to.core.Island._
import pl.edu.agh.to.core.Reaper.WatchMe
import pl.edu.agh.to.operators.{Operator, iOperator}

import scala.collection.mutable.{Set => MSet}
import scala.collection.parallel.mutable.ParSet
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

class Island(operator: iOperator, roundsPerTick: Int) extends Actor {
  import context.dispatcher
  private val agents: ParSet[Agent] = ParSet.empty
  private var reaper: ActorRef = _

  override def receive: Receive = {
    case Initialize(initAgents, r) =>
      agents ++= initAgents
      reaper = r
      reaper ! WatchMe(self)
    case Summary =>
      sender() ! (if (agents.nonEmpty) AgentMessage(agents.maxBy(_.getFitness)) else ())

    case Tick =>
      val toSend = sender()
      Future {
        val newState = (0 until roundsPerTick).foldLeft(agents.to[Set])((a, _) => singleIteration(a))
        agents.clear()
        agents ++= newState
      }.onComplete {
        case Success(_) =>
          toSend ! Success()
        case Failure(t) =>
          toSend ! Failure(new ExecutionException("Tick calculations failed due to: ", t))
      }
    case AgentMessage(agent) =>
      agents += agent

    case Migrate(isle) =>
      isle ! (if (agents.nonEmpty) AgentMessage(agents.maxBy(_.getFitness)) else ())

    case Stop =>
      context.stop(self)
  }

  def singleIteration(agents: Set[Agent]): Set[Agent] = {
    val agentsSnapshot = agents.to[MSet]
    agentsSnapshot.toSeq.sortBy(_ => Random.nextInt()).grouped(2).foreach {s =>
      if (s.size == 2) {
        Island.temporaryExchange(s.head, s(1))
      }
    }
    agentsSnapshot.retain(_.isAlive)
//    val mutationNumber = Random.nextInt(agents.size / 5) + 1
//    for (agent <- agentsSnapshot.toStream.sortBy(_ => Random.nextInt()).take(mutationNumber)) {
//      operator.mutate(agent, Random.nextInt(4) + 1)
//    }
//    agentsSnapshot.retain(_.isAlive)
    val newGeneration = agentsSnapshot.toStream.sortBy(_ => Random.nextInt()).grouped(2).collect {
      case Stream(a, b) =>
        operator.copulate(a, b)
    }.toSet
    newGeneration | agentsSnapshot
  }
}

object Island {

  case class Migrate(isle: ActorRef)

  case object Stop

  case class AgentMessage(agent: Agent)

  case object Tick

  case object Summary

  case class Initialize(agents: Seq[Agent], reaper: ActorRef)


  def temporaryExchange(a: Agent, b: Agent): Unit = {
    val (winner, looser) = if (a.getFitness >= b.getFitness) (a, b) else (b, a)
    val transferredEnergy = Math.min(looser.getEnergy, 1)
    winner.setEnergy(winner.getEnergy + transferredEnergy)
    looser.setEnergy(looser.getEnergy - transferredEnergy)
  }

  def props(operator: Operator, roundsPerTick: Int): Props = Props(new Island(operator, roundsPerTick))
}
