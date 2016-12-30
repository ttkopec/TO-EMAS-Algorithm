package pl.edu.agh.to.operators;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SelectionTest {
    private AgentConfig agentConfig = new AgentConfig(50, 200, 0);
    private Agent agent;
    private SelectionOperator selectionOperator = new SelectionOperator();

    @Before
    public void beforeEach() {
        agent = new Agent(null, 0, agentConfig);
    }

    @Test
    public void testArguments() {
        assertFalse("Incorrect number of arguments: 0", selectionOperator.checkTypes());
        assertFalse("Incorrect number of arguments: 3", selectionOperator.checkTypes(agent, 1, null));
        assertFalse("First argument not an instance of Agent", selectionOperator.checkTypes(1, agent));
        assertFalse("Second argument not an instance of Integer", selectionOperator.checkTypes(agent, null));
        assertTrue(selectionOperator.checkTypes(agent, 1));
    }

    @Test
    public void testExecution() {
        int threshold = 0;
        double energy = 1d;
        agent.setEnergy(energy);
        assertTrue(String.format("Agent should be alive for threshold: {%1$d} and energy: {%2$.2f}", threshold, energy),
                (Boolean) selectionOperator.execute(agent, threshold));

        energy = -1d;
        agent.setEnergy(energy);
        assertFalse(String.format("Agent should be dead for threshold: {%1$d} and energy: {%2$.2f}", threshold, energy),
                (Boolean) selectionOperator.execute(agent, threshold));

    }
}
