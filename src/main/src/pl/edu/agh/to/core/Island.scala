package pl.edu.agh.to.core

import akka.actor.{Actor, ActorRef, Props}
import pl.edu.agh.to.agent.Agent
import pl.edu.agh.to.core.Island._
import pl.edu.agh.to.core.Reaper.WatchMe
import pl.edu.agh.to.core.util.{TrieSet, TypedOperatorsConfig}

import scala.collection.mutable.{Set => MSet}
import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}

class Island(config: TypedOperatorsConfig[Agent], roundsPerTick: Int) extends Actor {

  import context.dispatcher

  private val agents = TrieSet.empty[Agent]
  private var reaper: ActorRef = _

  override def receive: Receive = {
    case Initialize(initAgents: Seq[Agent], r) =>
      agents ++= initAgents
      reaper = r
      reaper ! WatchMe(self)
    case Summary =>
      sender() ! (if (agents.nonEmpty) {
        AgentMessage(agents.maxBy(config.evaluationOperator))
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
      isle ! AgentMessage(agents.maxBy(config.evaluationOperator))

    case Stop =>
      context.stop(self)
  }

  def singleIteration: Unit = {
    val newGeneration = agents.toStream.sortBy(_ => Random.nextInt()).grouped(2).flatMap {
      case a #:: b #:: Stream.Empty =>
        Try(config.crossOverOperator(a, b)) match {
          case Success(opt) => opt
          case Failure(_) => None
        }
      case _ =>
        None
    } map config.mutationOperator

    agents ++= newGeneration
    agents.retain(config.selectionOperator)
  }
}

object Island {

  case class Migrate(isle: ActorRef)

  case object Stop

  case class AgentMessage(agent: Agent)

  case object Tick

  case object Summary

  case class Initialize(agents: Seq[Agent], reaper: ActorRef)

  def props[A](config: TypedOperatorsConfig[Agent], roundsPerTick: Int): Props = Props(new Island(config, roundsPerTick))
}
