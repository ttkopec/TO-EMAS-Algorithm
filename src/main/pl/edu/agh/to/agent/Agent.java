package pl.edu.agh.to.agent;

import pl.edu.agh.to.genotype.Genotype;
import pl.edu.agh.to.operators.Operator;

public class Agent {

    private Genotype genotype;
    private double energy;
    private Operator operator;

    public Agent(Genotype genotype, double energy, Operator operator) {
        this.genotype = genotype;
        this.energy = energy;
        this.operator = operator;

    }

    public Genotype getGenotype() {
        return genotype;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;

    }

    public double getFitness() {
        return operator.evaluation(this);
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return operator.selection(this);
    }

    public Agent mutate(int degree) {
        return operator.mutate(this, degree);
    }

    public Agent cross(Agent entity){
        return operator.copulate(this, entity);
    }
}