package pl.edu.agh.to.agent;

import pl.edu.agh.to.genotype.Genotype;
import pl.edu.agh.to.operators.Operators;

public class Agent {

    private Genotype genotype;
    private double energy;

    private AgentConfig config;

    public Agent(Genotype genotype, double energy, AgentConfig config) {
        this.genotype = genotype;
        this.energy = energy;
        this.config = config;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;

    }

    public int getFitness() {
        return config.getOperator().evaluation(this);
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return config.getDeathEnergy() < energy;
    }

    public AgentConfig getConfig() {
        return config;
    }
}