package pl.edu.agh.to.action;

import pl.edu.agh.to.Operator;
import pl.edu.agh.to.agent.Agent;

import java.util.Optional;

public class Interaction {
    private final Agent firstAgent;
    private final Agent secondAgent;

    public Interaction(Agent firstAgent, Agent secondAgent) {
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
    }

    public Optional<Agent> interact(){
        Operator o = new Operator() {
            @Override
            public Optional<Agent> operate(Agent firstAgent, Agent secondAgent) {
                return null;
            }
        };
        return o.operate(firstAgent, secondAgent);
    }

    public Agent getFirstAgent() {
        return firstAgent;
    }

    public Agent getSecondAgent() {
        return secondAgent;
    }
}
