package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;

/**
 * Created by krzys on 13.12.2016.
 */
public class SelectionOperator {

    Operators rootOperators;

    SelectionOperator(Operators rootOperators){
        this.rootOperators=rootOperators;
    }
    public boolean selection(Agent subject, double tresholdEnergy){

        return subject.getEnergy() > tresholdEnergy;
    }
}
