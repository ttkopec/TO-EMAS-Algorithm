package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;


/**
 * Created by krzys on 04.12.2016.
 */
public interface iOperators {

    int evaluation(Agent subject);

    boolean selection(Agent subject, double tresholdEnergy);

    Agent crossOver(Agent father, Agent mother, double startingEnergy);

    Agent mutation(Agent subject, int degree);

}