package pl.edu.agh.to.action;

import pl.edu.agh.to.agent.Agent;

public class Interaction {
    private final Agent agent;
    private final Agent agent2;

    public Interaction(Agent agent, Agent agent2) {
        this.agent = agent;
        this.agent2 = agent2;
    }

    public void interact(){

    }

    public Agent getAgent() {
        return agent;
    }

    public Agent getAgent2() {
        return agent2;
    }
}
