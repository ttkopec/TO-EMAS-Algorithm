package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.genotype.Genotype;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by krzys on 13.12.2016.
 */
public class CrossOverOperator {

    Operators rootOperators;

    CrossOverOperator(Operators rootOperators){
        this.rootOperators=rootOperators;
    }

    public Agent crossOver(Agent father, Agent mother, double startingEnergy) {
        AgentConfig config = father.getConfig();
        if(startingEnergy/2>father.getEnergy() || startingEnergy/2>mother.getEnergy())
            throw new IllegalArgumentException("Parents dont have enough energy to sustain new child");

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
            int choice = abs(rand.nextInt() % 2);
            if (choice == 1)
                genotype.append(fatherGen.charAt(i));
            else
                genotype.append(motherGen.charAt(i));
        }
        for (int j = i; j < maxSize; j++)
            genotype.append(longerGenotype.charAt(j));
        Double fatherTransfer = Math.min(father.getEnergy(), config.getReproductionEnergy());
        Double motherTransfer = Math.min(mother.getEnergy(), config.getReproductionEnergy());
        father.setEnergy(father.getEnergy() - fatherTransfer);
        mother.setEnergy(mother.getEnergy() - motherTransfer);
        double childEnergy = fatherTransfer + motherTransfer;
        return new Agent(new Genotype(new ArrayList<Double>()), childEnergy, new AgentConfig(100, 20, 0, new Operators()));
    }
}
