package pl.edu.agh.to.core

import akka.actor.{Actor, ActorRef, Props}
import pl.edu.agh.to.agent.Agent
import pl.edu.agh.to.core.Island._
import pl.edu.agh.to.core.Reaper.WatchMe
import pl.edu.agh.to.operators.{Operators, iOperators}

import scala.collection.mutable.{Set => MSet}
import scala.concurrent.Future
import scala.util.Random

class Island(operator: iOperators, roundsPerTick: Int) extends Actor {

  import context.dispatcher

  private val agents: MSet[Agent] = MSet.empty
  private var reaper: ActorRef = _

  override def receive: Receive = {
    case Initialize(initAgents, r) =>
      agents.synchronized {
        agents ++= initAgents
      }
      reaper = r
      reaper ! WatchMe(self)
    case Summary =>
      sender() ! (if (agents.nonEmpty) {
        agents.synchronized {
          AgentMessage(agents.maxBy(_.getFitness))
        }
      } else {
        ()
      })

    case Tick =>
      Future {
        val newState = (0 until roundsPerTick).foldLeft(agents.synchronized(agents.toSet))((a, _) => singleIteration(a))
        agents.synchronized {
          agents.clear()
          agents ++= newState
        }
      }
    case AgentMessage(agent) =>
      agents.synchronized {
        agents += agent
      }

    case Migrate(isle) =>
      isle ! agents.synchronized {
        agents.maxBy(_.getEnergy)
      }

    case Stop =>
      context.stop(self)
  }

  def singleIteration(agents: Set[Agent]): Set[Agent] = {
    val agentsSnapshot = agents.to[MSet]
    agentsSnapshot ++= agents.toStream.sortBy(_ => Random.nextInt()).grouped(2).collect {
      case a #:: b #:: Stream.Empty =>
        val config = a.getConfig
        config.getOperators.crossOver(a, b, 0)
    }
    agentsSnapshot.retain(_.isAlive)
    agentsSnapshot.toSet
  }
}

object Island {

  case class Migrate(isle: ActorRef)

  case object Stop

  case class AgentMessage(agent: Agent)

  case object Tick

  case object Summary

  case class Initialize(agents: Seq[Agent], reaper: ActorRef)

  def props(operator: Operators, roundsPerTick: Int): Props = Props(new Island(operator, roundsPerTick))
}
