package pl.edu.agh.to.core

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import pl.edu.agh.to.agent.Agent
import pl.edu.agh.to.genotype.Genotype
import pl.edu.agh.to.operators.Operator

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}

class CoreSystem(islandsNumber: Int,
                 roundsPerTick: Int,
                 islandPopulation: Int,
                 operator: Operator,
                 agentProvider: Operator => Agent) {

  import Island._

  val actorSystem = ActorSystem("EMAS_simulation")
  val islandProps = Island.props(operator, roundsPerTick)
  implicit val timeout = Timeout(2 seconds)
  val reaper = actorSystem.actorOf(Props[Reaper], s"Reaper")
  val islands = for (i <- 1 to islandsNumber) yield actorSystem.actorOf(islandProps, s"Island_$i")
  val delay = islandPopulation * roundsPerTick * 20 micros

  //initialize simulation
  for (island <- islands) {
    island ! Initialize((0 until islandPopulation).map(_ => agentProvider(operator)), reaper)
  }

  //schedule simulation epochs execution
  actorSystem.scheduler.schedule(10 millis, delay) {
    for (island <- islands) island ! Tick
  }

  //schedule agents migration
  actorSystem.scheduler.schedule(2 seconds, 400 millis) {
    islands.sortBy(_ => Random.nextInt()).grouped(2).collect {
      case IndexedSeq(a, b) =>
        a ! Migrate(b)
    }
  }

  //schedule simulation ending
  actorSystem.scheduler.scheduleOnce(10 seconds) {
    Future.sequence(islands.map(_ ? Summary)).map(_.collect{
      case AgentMessage(agent) =>
        agent
    }).onComplete {
      case Success(agents) if agents.nonEmpty =>
        val bestFitnes = agents.maxBy(_.getFitness)
        println(s"Simulation finished with fitness level: ${bestFitnes.getFitness} and genotype: ${bestFitnes.getGenotype.getGenotyp}")
        islands.foreach(_ ! Stop)
      case Success(agents) =>
        println("No agents in system")
        actorSystem.terminate()
        islands.foreach(_ ! Stop)
      case Failure(cause) =>
        println(s"Simluation failed due to:")
        cause.printStackTrace()
        actorSystem.terminate()
        islands.foreach(_ ! Stop)
    }
  }


}

object CoreSystem {

  private val testOperator = new Operator()

  private def testAgentProvider(operator: Operator): Agent = {
    val genotype = new Genotype((Random.nextDouble() * 10).toString)
    new Agent(genotype, 100, operator)
  }


  def main(args: Array[String]): Unit = {
      val system = new CoreSystem(10, 20, 30, testOperator, testAgentProvider)
  }
}