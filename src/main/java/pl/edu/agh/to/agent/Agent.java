package pl.edu.agh.to.agent;

import pl.edu.agh.to.FitnessEvaluator;
import pl.edu.agh.to.genotype.Genotype;

public class Agent {

    private final Genotype genotype;
    private final FitnessEvaluator evaluator;
    private final double minimalEnergy;
    private double energy;

    public Agent(Genotype genotype, FitnessEvaluator evaluator, double energy, double minimalEnergy) {
        this.genotype = genotype;
        this.evaluator = evaluator;
        this.energy = energy;
        this.minimalEnergy = minimalEnergy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public double getFitness() {
        return evaluator.evaluate(genotype);
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return energy >= minimalEnergy;
    }

}
