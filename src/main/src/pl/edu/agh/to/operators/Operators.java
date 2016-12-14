package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;

import static java.lang.Math.abs;

public class Operators implements iOperators {

    private CrossOverOperator crossOverOperator=new CrossOverOperator(this);
    private SelectionOperator selectionOperator=new SelectionOperator(this);
    private MutationOperator mutationOperator=new MutationOperator(this);
    private EvaluationOperator evaluationOperator=new EvaluationOperator(this);

    public int evaluation(Agent subject) {
       return evaluationOperator.evaluation(subject);
    }
    public boolean selection(Agent subject, double tresholdEnergy){
        return selectionOperator.selection(subject,tresholdEnergy);
    }
    public Agent mutation(Agent subject, int degree) {
      return mutationOperator.mutate(subject,degree);
    }
    public Agent crossOver(Agent father, Agent mother, double startingEnergy) {
        return this.crossOverOperator.crossOver(father,mother,startingEnergy);
    }
}
