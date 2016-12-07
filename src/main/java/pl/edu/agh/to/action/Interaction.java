package pl.edu.agh.to.action;


import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.operators.Operator;

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
        Operator o = new Operator();
        Optional<Agent> result = Optional.empty();
        if(firstAgent.getEnergy() > firstAgent.getConfig().getReproductionEnergy()
                && secondAgent.getEnergy() > secondAgent.getConfig().getReproductionEnergy()
                && firstAgent.getEnergy() + secondAgent.getEnergy() > firstAgent.getConfig().getStartEnergy()) {
            Agent newAgent = o.copulate(firstAgent, secondAgent);
            newAgent = o.mutate(newAgent, new Random().nextInt() % 100);
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
