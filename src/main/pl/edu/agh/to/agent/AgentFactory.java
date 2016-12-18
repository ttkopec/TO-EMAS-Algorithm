package pl.edu.agh.to.agent;


import pl.edu.agh.to.genotype.Genotype;

public class AgentFactory {
    private final AgentConfig agentConfig;

    public AgentFactory(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    public Agent createAgent(Genotype genotype, double energy) {
        return new Agent(genotype, energy, agentConfig);
    }
}
