package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;

import java.util.List;

/**
 * Created by krzys on 13.12.2016.
 */
public class EvaluationOperator implements Operator {
    private final int A = 10;

    public Object execute(Object... args) {
        if (!checkTypes(args))
            throw new IllegalArgumentException("Type checking of Arguments failed");
        Agent subject = (Agent) args[0];
        List<Double> genotype = subject.getGenotype().getGenotyp();
        double fitness = A * genotype.size();
        int size = genotype.size();
        for (int i = 0; i < size; i++) {
            double value = genotype.get(i);
            fitness += Square(value) - A * Math.cos(2 * Math.PI * value);
        }
        return fitness;
    }

    public boolean checkTypes(Object... args) {
        if (args.length == 1 && args[0] instanceof Agent) {
            Agent subject = (Agent) args[0];
            if (subject.getGenotype().getGenotyp() instanceof List)
                return true;
        }
        return false;
    }

    private double Square(double a) {
        return a * a;
    }
}
