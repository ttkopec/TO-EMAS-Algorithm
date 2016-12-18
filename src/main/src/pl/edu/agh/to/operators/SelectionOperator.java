package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;

/**
 * Created by krzys on 13.12.2016.
 */
public class SelectionOperator implements Operator {



    public Object execute(Object ... args){
        if(!checkTypes(args))
            throw new IllegalArgumentException("Type checking of Arguments failed");
        Agent subject= (Agent) args[0];
        int thereshold= (Integer) args[1];
        return subject.getEnergy()>thereshold;
    }
    public boolean checkTypes(Object ... args){
        if(args.length==2 && args[0] instanceof  Agent && args[1] instanceof Integer)
            return true;
        return false;
    }
}
