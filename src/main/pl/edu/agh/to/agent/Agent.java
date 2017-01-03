package pl.edu.agh.to.agent;

import pl.edu.agh.to.genotype.Genotype;

public class Agent {

    private Genotype genotype;
    private int energy;

    private AgentConfig config;

    public Agent(Genotype genotype, int energy, AgentConfig config) {
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
    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return config.getDeathEnergy() < energy;
    }

    public AgentConfig getConfig() {
        return config;
    }
}