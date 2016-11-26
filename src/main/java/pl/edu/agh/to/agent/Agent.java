package pl.edu.agh.to.agent;

import pl.edu.agh.to.genotype.Genotype;

public class Agent {

    private final Genotype genotype;

    public Agent(Genotype genotype) {
        this.genotype = genotype;
    }

    public Genotype getGenotype() {
        return genotype;
    }
}
