package pl.edu.agh.to;

import pl.edu.agh.to.genotype.Genotype;

@FunctionalInterface
public interface FitnessEvaluator {

    double evaluate(Genotype genotype);
}
