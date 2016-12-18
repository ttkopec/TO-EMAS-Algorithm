package pl.edu.agh.to.action;


import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.operators.CrossOverOperator;
import pl.edu.agh.to.operators.MutationOperator;

import java.util.Optional;
import java.util.Random;

public class Interaction {
    private final Agent firstAgent;
    private final Agent secondAgent;

    public Interaction(Agent firstAgent, Agent secondAgent) {
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
    }

    public Optional<Agent> interact(){
        CrossOverOperator crossOverOperator = new CrossOverOperator();
        MutationOperator mutationOperator=new MutationOperator();
        Optional<Agent> result = Optional.empty();
        if(firstAgent.getEnergy() > firstAgent.getConfig().getReproductionEnergy()
                && secondAgent.getEnergy() > secondAgent.getConfig().getReproductionEnergy()
                && firstAgent.getEnergy() + secondAgent.getEnergy() > firstAgent.getConfig().getStartEnergy()) {
            Agent newAgent = (Agent)crossOverOperator.execute(firstAgent, secondAgent, 0);
            newAgent = (Agent)mutationOperator.execute(newAgent, new Random().nextInt() % 100);
            result = Optional.of(newAgent);
        }
        return result;
    }

    public Agent getFirstAgent() {
        return firstAgent;
    }

    public Agent getSecondAgent() {
        return secondAgent;
    }
}
