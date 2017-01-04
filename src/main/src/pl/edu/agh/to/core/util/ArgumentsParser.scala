package pl.edu.agh.to.core.util

import java.io.FileInputStream
import java.util.Properties

import pl.edu.agh.to.core.CoreSystem
import pl.edu.agh.to.operators._

object ArgumentsParser {

  private val usage = "Usage: [--islandsNumber num] [--islandInitialPopulation num] [--simulationRoundsRerTick num]" +
    "[--crossOver className] [--evaluation className] [--mutation className] [--selection className]\n" +
    "In case of lack of one of the options default value is taken (more information in documentation), order of arguments is irrelevant\n" +
    "Falling back to default settings"

  private case class ParseState(islandNumb: Int = 10,
                        islandPopulation: Int = 30,
                        roundsPerTick: Int = 20,
                        crossOverOperator: Operator = new CrossOverOperator,
                        evaluationOperator: Operator = new EvaluationOperator,
                        mutationOperator: Operator = new MutationOperator,
                        selectionOperator: Operator = new SelectionOperator
                       ) {

    def getNumericConstants: NumericConstants = NumericConstants(islandNumb, islandPopulation, roundsPerTick)

  }

  case class NumericConstants(islandNumb: Int, islandPopulation: Int, roundsPerTick: Int)

  def parseArgs(args: List[String]): (NumericConstants, OperatorsConfig) = {
    if (args.isEmpty) println(usage)

    def nextOption(state: ParseState, leftInput: List[String]): ParseState = {
      leftInput match {
        case Nil =>
          state
        case "--islandsNumber" :: value :: tail =>
          nextOption(state.copy(islandNumb = value.toInt), tail)
        case "--islandInitialPopulation" :: value :: tail =>
          nextOption(state.copy(islandPopulation = value.toInt), tail)
        case "--simulationRoundsRerTick" :: value :: tail =>
          nextOption(state.copy(roundsPerTick = value.toInt), tail)
        case "--crossOver" :: value :: tail =>
          nextOption(state.copy(crossOverOperator = OperatorType.valueOf(value).getInstance()), tail)
        case "--evaluation" :: value :: tail =>
          nextOption(state.copy(evaluationOperator = OperatorType.valueOf(value).getInstance()), tail)
        case "--mutation" :: value :: tail =>
          nextOption(state.copy(mutationOperator = OperatorType.valueOf(value).getInstance()), tail)
        case "--selection" :: value :: tail =>
          nextOption(state.copy(selectionOperator = OperatorType.valueOf(value).getInstance()), tail)
        case _ =>
          println(usage)
          state
      }

    }

    val prop = new Properties()
    prop.load(new FileInputStream(CoreSystem.PropertiesFile))

    val initialState = ParseState(
      islandNumb = prop.getProperty(CoreSystem.IslandsNumberProperty).toInt,
      islandPopulation = prop.getProperty(CoreSystem.IslandPopulationProperty).toInt,
      roundsPerTick = prop.getProperty(CoreSystem.RoundsPerTickProperty).toInt,
      crossOverOperator = OperatorType.valueOf(prop.getProperty(CoreSystem.CrossOverOperatorProperty)).getInstance(),
      evaluationOperator = OperatorType.valueOf(prop.getProperty(CoreSystem.EvaluationOperatorProperty)).getInstance(),
      mutationOperator = OperatorType.valueOf(prop.getProperty(CoreSystem.MutationOperatorProperty)).getInstance(),
      selectionOperator = OperatorType.valueOf(prop.getProperty(CoreSystem.SelectionOperatorProperty)).getInstance()
    )
    val parseEffect = nextOption(initialState, args)

    val config = new OperatorsConfig {
      override def crossOverOperator: Operator = parseEffect.crossOverOperator

      override def mutationOperator: Operator = parseEffect.mutationOperator

      override def selectionOperator: Operator = parseEffect.selectionOperator

      override def evaluationOperator: Operator = parseEffect.evaluationOperator
    }

    (parseEffect.getNumericConstants, config)
  }

}
