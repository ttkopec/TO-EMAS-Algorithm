package pl.edu.agh.to.action;


import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.operators.CrossOverOperator;
import pl.edu.agh.to.operators.MutationOperator;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Interaction {
    private final Agent firstAgent;
    private final Agent secondAgent;
    private Optional<Agent> child;

    public Interaction(Agent firstAgent, Agent secondAgent) {
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
        this.child = Optional.empty();
    }

    public Optional<Agent> interact(){
        AgentConfig config = firstAgent.getConfig();
        Optional<Agent> result = Optional.empty();

        if(firstAgent.getEnergy() > config.getReproductionEnergy()
                && secondAgent.getEnergy() > config.getReproductionEnergy()
                && firstAgent.getEnergy() + secondAgent.getEnergy() > config.getStartEnergy()
                ) {

            Agent newAgent = (Agent) config.getCrossOverOp().execute(firstAgent, secondAgent, config.getStartEnergy());
            newAgent = (Agent) config.getMutationOp().execute(newAgent, new Random().nextInt() % 100);
            result = Optional.of(newAgent);
        } else {
            Float agent1Fitness = (Float) config.getEvaluationOp().execute(firstAgent);
            Float agent2Fitness = (Float) config.getEvaluationOp().execute(secondAgent);

            Float fitnessDiff = agent1Fitness - agent2Fitness;
            if(fitnessDiff > 0) {
                int energyChange = (int) (secondAgent.getEnergy() * 0.1);
                firstAgent.setEnergy(firstAgent.getEnergy() + energyChange);
                secondAgent.setEnergy(firstAgent.getEnergy() - energyChange);
            }
        }

        child = result;
        return result;
    }

    public List<Agent> detectDeadAgents() {
        AgentConfig config = firstAgent.getConfig();
        List<Agent> deadAgents = new LinkedList<>();
        if(firstAgent.getEnergy() < config.getDeathEnergy()) {
            deadAgents.add(firstAgent);
        }
        if(secondAgent.getEnergy() < config.getDeathEnergy()) {
            deadAgents.add(secondAgent);
        }
        if(child.isPresent() && child.get().getEnergy() < config.getDeathEnergy()) {
            deadAgents.add(child.get());
        }
        return deadAgents;
    }


    public Agent getFirstAgent() {
        return firstAgent;
    }

    public Agent getSecondAgent() {
        return secondAgent;
    }
}
