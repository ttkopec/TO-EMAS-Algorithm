package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.genotype.Genotype;

import java.util.Random;

/**
 * Created by krzys on 04.12.2016.
 */
public class Operator implements iOperator {

    public double evaluation(Agent subject) {
        return Double.parseDouble(subject.getGenotype().toString());
    }

    public boolean selection(Agent subject) {
        if (subject.getEnergy() > 0)
            return true;
        else
            return false;
    }

    public Agent mutate(Agent subject, int degree) {
        String genotype = subject.getGenotype().toString();
        StringBuilder mutation = new StringBuilder(genotype);
        Random rand = new Random();

        for (int i = 0; i < degree; i++) {
            int index = rand.nextInt() % genotype.length();
            int value = rand.nextInt() % 9 + 1;
            mutation.replace(index, index + 1, Integer.toString(value));
        }
        subject.setGenotype(new Genotype(mutation.toString()));
        return subject;
    }

    public Agent copulate(Agent father, Agent mother) {

        StringBuilder genotype = new StringBuilder();
        Random rand = new Random();

        String motherGen = mother.getGenotype().toString();
        String fatherGen = father.getGenotype().toString();

        int fatherSize = fatherGen.length();
        int motherSize = motherGen.length();
        int minSize = Math.min(motherSize, fatherSize);
        int maxSize = Math.max(motherSize, fatherSize);
        String longerGenotype;

        if (fatherSize > motherSize)
            longerGenotype = fatherGen;
        else
            longerGenotype = motherGen;

        int i;
        for (i = 0; i < minSize; i++) {
            int choice = rand.nextInt() % 2;
            if (choice == 1)
                genotype.append(fatherGen.charAt(i));
            else
                genotype.append(motherGen.charAt(i));
        }
        for (int j = i; j < maxSize; j++)
            genotype.append(longerGenotype.charAt(j));
        father.setEnergy(father.getEnergy() / 2);
        mother.setEnergy(mother.getEnergy() / 2);
        double childEnergy = (mother.getEnergy() + father.getEnergy()) / 2;
        return new Agent(new Genotype(genotype.toString()), childEnergy, this);
    }
}
