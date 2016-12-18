package pl.edu.agh.to.core

import akka.actor.{Actor, ActorRef, Props}
import pl.edu.agh.to.agent.Agent
import pl.edu.agh.to.core.Island._
import pl.edu.agh.to.core.Reaper.WatchMe
import pl.edu.agh.to.core.util.TrieSet
import pl.edu.agh.to.operators.{Operators, iOperators}

import scala.collection.mutable.{Set => MSet}
import scala.concurrent.Future
import scala.util.{Random, Try}

class Island(operator: iOperators, roundsPerTick: Int) extends Actor {

  import context.dispatcher

  private val agents = TrieSet.empty[Agent]
  private var reaper: ActorRef = _

  override def receive: Receive = {
    case Initialize(initAgents, r) =>
      agents ++= initAgents
      reaper = r
      reaper ! WatchMe(self)
    case Summary =>
      sender() ! (if (agents.nonEmpty) {
        AgentMessage(agents.maxBy(_.getFitness))
      } else {
        ()
      })

    case Tick =>
      Future {
        for (i <- 0 until roundsPerTick) {
          singleIteration
        }
      }
    case AgentMessage(agent) =>
      agents += agent

    case Migrate(isle) =>
      isle ! AgentMessage(agents.maxBy(_.getEnergy))

    case Stop =>
      context.stop(self)
  }

  def singleIteration: MSet[Agent] = {
    val newGeneration = agents.toStream.sortBy(_ => Random.nextInt()).grouped(2).flatMap {
      case a #:: b #:: Stream.Empty =>
        val config = a.getConfig
        Try(config.getOperators.crossOver(a, b, config.getReproductionEnergy)).toOption
      case _ =>
        None
    }
    agents.retain(_.isAlive)
    agents ++= newGeneration
    agents.filter(a => a.getEnergy > a.getConfig.getStartEnergy)
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
