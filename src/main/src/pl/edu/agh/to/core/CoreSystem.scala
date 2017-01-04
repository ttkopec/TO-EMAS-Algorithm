package pl.edu.agh.to.core

import java.io.FileInputStream
import java.util.Properties

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import pl.edu.agh.to.agent.{Agent, AgentConfig, AgentFactory}
import pl.edu.agh.to.core.util.ArgumentsParser.NumericConstants
import pl.edu.agh.to.core.util.{ArgumentsParser, OperatorsConfig}
import pl.edu.agh.to.genotype.Genotype
import pl.edu.agh.to.operators._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}
import scala.collection.JavaConverters._

class CoreSystem(islandsNumber: Int,
                 roundsPerTick: Int,
                 islandPopulation: Int,
                 algorithmName: String,
                 config: OperatorsConfig,
                 agentProvider: OperatorsConfig => Agent) {

  import Island._

  println("Init " + algorithmName)
  private val typedConfig = config.typedConfig[Agent]
  val actorSystem = ActorSystem("EMAS_simulation")
  val islandProps = Island.props(typedConfig, roundsPerTick)
  implicit val timeout = Timeout(2 seconds)
  val reaper = actorSystem.actorOf(Props[Reaper], s"Reaper")
  val islands = for (i <- 1 to islandsNumber) yield actorSystem.actorOf(islandProps, s"Island_$i")
  val delay = islandPopulation * roundsPerTick * 20 micros

  //initialize simulation
  for (island <- islands) {
    island ! Initialize((0 until islandPopulation).map(_ => agentProvider(config)), reaper)
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
        val bestFitness = agents.maxBy(typedConfig.evaluationOperator)
        println(s"Simulation finished with fitness level: ${typedConfig.evaluationOperator(bestFitness)} " +
          s"and genotype: ${bestFitness.getGenotype.getGenotyp.asScala.mkString("[ ", ", ", "]")}")
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

  val PropertiesFile = "src/main/resources/emas.properties"
  val AlgorithmNameProperty = "algorithm-name"
  val IslandsNumberProperty = "islands-number"
  val RoundsPerTickProperty = "rounds-per-tick"
  val IslandPopulationProperty = "island-population"
  val CrossOverOperatorProperty = "cross-over"
  val EvaluationOperatorProperty = "evaluation"
  val MutationOperatorProperty = "mutation"
  val SelectionOperatorProperty = "selection"


  def agentProvider(agentFactory: AgentFactory, genotypeProvider: () => Genotype)(operatorsConfig: OperatorsConfig): Agent = {
    agentFactory.createAgent(genotypeProvider())
  }

  def testGenotypeProvider(dim: Int): Genotype = new Genotype((0 until dim).map(_ => Double.box(Random.nextDouble())).asJava)


  def main(args: Array[String]): Unit = {

    val (NumericConstants(islandNumber, islandPopulation, roundsPerTick), operatorConfig) = ArgumentsParser.parseArgs(args.toList)

    val testAgentConfig = new AgentConfig(50, 200, 0)

    val testAgentFactory = new AgentFactory(testAgentConfig)

    val system = new CoreSystem(islandsNumber = islandNumber,
      roundsPerTick = roundsPerTick,
      islandPopulation = islandPopulation,
      algorithmName = "emas",
      config = operatorConfig,
      agentProvider = agentProvider(testAgentFactory, () => testGenotypeProvider(10)))
  }
}
