package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.genotype.Genotype;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by krzys on 13.12.2016.
 */
public class MutationOperator {

    Operators rootOperators;

    MutationOperator(Operators rootOperators){
        this.rootOperators=rootOperators;

    }
    public Agent mutate(Agent subject, int degree) {
        String genotype = subject.getGenotype().toString();
        StringBuilder mutation = new StringBuilder(genotype);
        Random rand = new Random();

        for (int i = 0; i < degree; i++) {
            int index = abs(rand.nextInt() % mutation.length());
            int value = abs(rand.nextInt() % 9 + 1);
            mutation=mutation.replace(index, index + 1, Integer.toString(value));
        }
        subject.setGenotype(new Genotype(new ArrayList<Double>()));
        return subject;
    }
}
