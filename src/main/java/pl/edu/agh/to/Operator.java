package pl.edu.agh.to;

import pl.edu.agh.to.agent.Agent;

import java.util.Optional;

public interface Operator {

    Optional<Agent> operate(Agent firstAgent, Agent secondAgent);
}
