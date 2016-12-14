package pl.edu.agh.to.action;


import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.operators.Operators;

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
        Operators o = firstAgent.getConfig().getOperators();
        Optional<Agent> result = Optional.empty();
        if(firstAgent.getEnergy() > firstAgent.getConfig().getReproductionEnergy()
                && secondAgent.getEnergy() > secondAgent.getConfig().getReproductionEnergy()
                && firstAgent.getEnergy() + secondAgent.getEnergy() > firstAgent.getConfig().getStartEnergy()) {
            Agent newAgent = o.crossOver(firstAgent, secondAgent, 0);
            newAgent = o.mutation(newAgent, new Random().nextInt() % 100);
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
