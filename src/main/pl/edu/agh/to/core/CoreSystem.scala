package pl.edu.agh.to.core

import java.io.FileInputStream
import java.util
import java.util.Properties

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import pl.edu.agh.to.agent.{Agent, AgentConfig}
import pl.edu.agh.to.genotype.Genotype
import pl.edu.agh.to.operators.Operators

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}

class CoreSystem(islandsNumber: Int,
                 roundsPerTick: Int,
                 islandPopulation: Int,
                 algorithmName: String,
                 operator: Operators,
                 agentProvider: Operators => Agent) {

  import Island._

  println("Init " + algorithmName)

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
    Future.sequence(islands.map(_ ? Summary)).map(_.collect {
      case AgentMessage(agent) =>
        agent
    }).onComplete {
      case Success(agents) if agents.nonEmpty =>
        val bestFitnes = agents.maxBy(_.getFitness)
        println(s"Simulation finished with fitness level: ${bestFitnes.getFitness} and genotype: ${bestFitnes.getGenotype.get}")
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

  private val testOperator = new Operators()
  val PropertiesFile = "src/main/resources/emas.properties"
  val AlgorithmNameProperty = "algorithm-name"
  val IslandsNumberProperty = "islands-number"
  val RoundsPerTickProperty = "rounds-per-tick"
  val IslandPopulationProperty = "island-population"


  private def testAgentProvider(operator: Operators): Agent = {
    val genotype = new Genotype(new util.ArrayList[java.lang.Double]())
    val config = new AgentConfig(100, 20, 0, new Operators())
    new Agent(genotype, 20, config)

  }


  def main(args: Array[String]): Unit = {

    val prop = new Properties()
    prop.load(new FileInputStream(PropertiesFile))

    val (algorithmName, islandsNumber, roundsPerTick, islandPopulation) = try {
      (prop.getProperty(AlgorithmNameProperty),
        prop.getProperty(IslandsNumberProperty).toInt,
        prop.getProperty(RoundsPerTickProperty).toInt,
        prop.getProperty(IslandPopulationProperty).toInt)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        sys.exit(1)
    }

    val system = new CoreSystem(islandsNumber, roundsPerTick, islandPopulation, algorithmName, testOperator, testAgentProvider)
  }
}
