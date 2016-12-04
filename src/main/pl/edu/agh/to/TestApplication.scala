package pl.edu.agh.to

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.dispatch.ExecutionContexts
import pl.edu.agh.to.IslandActor.{Agent, MigrateAgents, Stop}

import scala.collection.mutable.{Set => MSet}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class IslandActor extends Actor {
  import IslandActor._
  import IslandUtils._

  private val agents = MSet.empty[Agent]

  override def receive: Receive = {
    case MigrateAgents(fit, islands) =>
      decrAgeAndRemoveDead()
      for ((agent, island) <- agents.toStream.filter(fit).zip(islands))
        island ! agent
    case agent @ Agent(_, _, _) =>
      decrAgeAndRemoveDead()
      println(s"$agent from ${sender()}")
      agents += agent
    case Stop =>
      context.system.terminate()
    case CrossOver(fun) =>
      decrAgeAndRemoveDead()
      agents ++= agents.getCrossoverSeq(fun)
      println(agents.mkString("New state: [" ," ,", " ]"))
  }

  private def decrAgeAndRemoveDead(): Unit = {
    agents.retain(_.counter > 0)
    for (agent <- agents) {
      agents -= agent
      agents += agent.copy(counter = agent.counter - 1)
    }
  }
}

object IslandActor {

  case class MigrateAgents(filterFun: Agent => Boolean, islands: Set[ActorRef])
  case class Agent(a: Double, b: Double, counter: Int = TestMain.defaultLifeSpan)
  case class CrossOver(crossingFunction: (Agent, Agent) => Option[Agent])
  case object Stop

  def getCrossOver(crossingFunction: (Agent, Agent) => Agent, filter: Agent => Boolean): CrossOver =
    CrossOver((a, b) => Some(crossingFunction(a, b)).filter(filter))
}

object TestMain {

  val defaultLifeSpan = 11

  def defaultCrosser(a: Agent, b: Agent): Agent = (a, b) match {
    case (Agent(a1, b1, _), Agent(a2, b2, _)) =>
      val a3 = (a1 + a2 + b1 + b2) / 4 + (Random.nextDouble() - Random.nextDouble()) * a1
      val b3 = (b2 + b1) / (2 * a3) * (a1 + a2) + Random.nextDouble() - Random.nextDouble() * b2
      Agent(a3, b3)
  }

  def filter(fitnessThreshold: Int, agent: Agent): Boolean = agent match {
    case Agent(a, b, _) =>
       3 * a + 2 * b > 5 * fitnessThreshold
  }

  /**
    * This method is only mock for now but illustrates the whole point of using akka:
    * Core: actor system, maintaining "simulation" clock
    * Messages are orders:
    * migratin, crossover, mutation etc, we can also easily update age of the actors, and kill the oldest
    * It is very easy to adjust any parameter of simulation desired, this also scales extremely well
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Test_system")
    implicit val context = ExecutionContexts.global()

    val defaultCrossOver = IslandActor.getCrossOver(defaultCrosser, filter(12, _))
    val a = system.actorOf(Props[IslandActor], "a")
    val b = system.actorOf(Props[IslandActor], "b")
    a ! Agent(10, 6)
    a ! Agent(7, 9)
    b ! Agent(12, 4)
    b ! Agent(8, 10)


    system.scheduler.schedule(10 milli, 1 second) {
      a ! defaultCrossOver
      b ! defaultCrossOver
    }

    system.scheduler.schedule(1 second, 1 second) {
      a ! MigrateAgents(filter(14, _), Set(b))
    }
    system.scheduler.schedule(1 second, 1 second) {
      b ! MigrateAgents(filter(14, _), Set(a))
    }

    system.scheduler.scheduleOnce(10 seconds) {
      a ! Stop
    }
  }

}

