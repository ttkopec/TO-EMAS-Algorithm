package pl.edu.agh.to.agent;

import pl.edu.agh.to.genotype.Genotype;

public class Agent {

    private final Genotype genotype;
    private final double fitness;
    private final double energy;


    public Agent(Genotype genotype, double fitness, double energy) {
        this.genotype = genotype;
        this.fitness = fitness;
        this.energy = energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public double getFitness() {
        return fitness;
    }

    public double getEnergy() {
        return energy;
    }
}
