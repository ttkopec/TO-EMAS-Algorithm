package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;

import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by krzys on 13.12.2016.
 */
public class MutationOperator implements Operator {

    public Object execute(Object ... args) {
        if (!checkTypes(args))
            throw new IllegalArgumentException("Type checking of Arguments failed");
        Agent subject = (Agent) args[0];
        int degree = (Integer) args[1];

        List genotype = (List) subject.getGenotype();

        Random rand = new Random();
        int size = genotype.size();
        for (int i = 0; i < degree; i++) {
            int index = rand.nextInt() % size;
            double value = rand.nextDouble();
            genotype.set(index, value);
        }
        return subject;
    }
    public boolean checkTypes(Object ... args){
        if(args.length==2 && args[0] instanceof  Agent && args[1] instanceof Integer)
            return true;
        return false;
    }
}
