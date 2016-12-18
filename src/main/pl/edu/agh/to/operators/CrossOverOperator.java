package pl.edu.agh.to.operators;

import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.genotype.Genotype;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by krzys on 13.12.2016.
 */
public class CrossOverOperator implements iOperator{

    public Object execute(Object ... args){
        if(!checkTypes(args))
            throw new IllegalArgumentException("Type checking of Arguments failed");
        Agent father= (Agent) args[0];
        Agent mother=(Agent) args[1];
        AgentConfig configFather = father.getConfig();
        AgentConfig configMother = mother.getConfig();

        Integer startingEnergy= (Integer)args[2];
        if(configFather.getReproductionEnergy() <startingEnergy/2 || configMother.getReproductionEnergy()<startingEnergy/2)
            throw new IllegalArgumentException("Parents dont have enough energy to support child");

       List   childGenotype=new ArrayList<>();
        Random rand=new Random();

        List motherGen = (List)mother.getGenotype();
        List fatherGen = (List)father.getGenotype();

        int fatherSize = fatherGen.size();
        int motherSize = motherGen.size();
        int minSize = Math.min(motherSize, fatherSize);
        int maxSize = Math.max(motherSize, fatherSize);
        List longerGenotype;

        if (fatherSize > motherSize)
            longerGenotype = fatherGen;
        else
            longerGenotype = motherGen;

        int i;
        for (i = 0; i < minSize; i++) {
            int choice = abs(rand.nextInt() % 2);
            if (choice == 1)
                childGenotype.add(fatherGen.get(i));
            else
                childGenotype.add(motherGen.get(i));
        }
        for (int j = i; j < maxSize; j++)
            childGenotype.add(longerGenotype.get(j));
        father.setEnergy(father.getEnergy() - startingEnergy/2);
        mother.setEnergy(mother.getEnergy() - startingEnergy/2);
        return new Agent(new Genotype(childGenotype), startingEnergy, new AgentConfig(100, 20, 0));
    }
    public boolean checkTypes(Object ... args){
        if(args.length==3 && args[0] instanceof  Agent && args[1] instanceof Agent && args[2] instanceof Integer) {
            Agent father= (Agent) args[0];
            Agent mother=(Agent) args[1];
            if (father.getGenotype() instanceof List && mother.getGenotype() instanceof List)
                return true;
        }
        return false;
    }
}
