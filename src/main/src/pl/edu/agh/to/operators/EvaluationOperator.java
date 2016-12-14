package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;

/**
 * Created by krzys on 13.12.2016.
 */
public class EvaluationOperator {
    Operators rootOperators;

    EvaluationOperator(Operators rootOperators){
        this.rootOperators=rootOperators;
    }
    public int evaluation(Agent subject) {
        return Integer.parseInt(subject.getGenotype().toString());
    }
}
