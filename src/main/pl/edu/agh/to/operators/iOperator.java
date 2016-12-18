package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;


/**
 * Created by krzys on 04.12.2016.
 */
public interface iOperator {
    Object execute(Object ... args);
    boolean checkTypes(Object ... args);

}