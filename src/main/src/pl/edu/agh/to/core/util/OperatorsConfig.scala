package pl.edu.agh.to.core.util

import java.util.Optional

import pl.edu.agh.to.operators.Operator

trait OperatorsConfig { self =>

  def evaluationOperator: Operator

  def crossOverOperator: Operator

  def mutationOperator: Operator

  def selectionOperator: Operator

  def typedConfig[A <: AnyRef] = new TypedOperatorsConfig[A] {
    override def evaluationOperator(agent: A): Double = self.evaluationOperator.execute(agent).asInstanceOf[Double]

    override def crossOverOperator(first: A, second: A): Option[A] = {
      val optAgent = self.crossOverOperator.execute(first, second).asInstanceOf[Optional[A]]
      if (optAgent.isPresent) {
        Some(optAgent.get())
      } else {
        None
      }
    }

    override def mutationOperator(agent: A): A = self.mutationOperator.execute(agent).asInstanceOf[A]

    override def selectionOperator(agent: A): Boolean = self.selectionOperator.execute(agent).asInstanceOf[Boolean]
  }

}

trait TypedOperatorsConfig[A <: AnyRef] {

  def evaluationOperator(agent: A): Double

  def crossOverOperator(first: A, second: A): Option[A]

  def mutationOperator(agent: A): A

  def selectionOperator(agent: A): Boolean
}
