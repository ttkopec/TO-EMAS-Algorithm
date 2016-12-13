package pl.edu.agh.to.agent;

import pl.edu.agh.to.genotype.Genotype;
import pl.edu.agh.to.operators.Operators;

public class Agent {

    private Genotype genotype;
    private double energy;
    private Operators operators;

    public Agent(Genotype genotype, double energy, Operators operators) {
        this.genotype = genotype;
        this.energy = energy;
        this.operators = operators;

    }

    public Genotype getGenotype() {
        return genotype;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;

    }

    public int getFitness() {
        return operators.evaluation(this);
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return operators.selection(this,0);
    }

    public Agent mutate(int degree) {
        return operators.mutation(this, degree);
    }

    public Agent cross(Agent entity){
        return operators.crossOver(this, entity,50);
    }
}