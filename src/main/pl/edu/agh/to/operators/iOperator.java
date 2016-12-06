package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;


/**
 * Created by krzys on 04.12.2016.
 */
public interface iOperator {

    double evaluation(Agent subject);

    boolean selection(Agent subject);

    Agent copulate(Agent father, Agent mother);

    Agent mutate(Agent subject, int degree);

}
