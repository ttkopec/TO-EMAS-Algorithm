package pl.edu.agh.to.operators;

import org.junit.Test;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.genotype.Genotype;

import static org.junit.Assert.*;

/**
 * Created by tkopec on 04.12.16.
 */
public class OperatorTest {
<<<<<<< HEAD
    private Operators operator = new Operators();
    private Agent agent = new Agent(null, 0, operator);
=======
    private Agent agent = new Agent(null, 0, new AgentConfig(0,0,0, new Operator()));
>>>>>>> fix tests

    @Test(expected = NumberFormatException.class)
    public void testEvaluation() {
        agent.setGenotype(new Genotype("numberFormatException"));
        agent.getFitness();
    }

    @Test
    public void testSelection() {
        agent.setEnergy(-1.0);
        assertFalse(agent.isAlive());

        agent.setEnergy(1.0);
        assertTrue(agent.isAlive());
    }

    @Test
<<<<<<< HEAD
    public void testMutation() {
        agent.setGenotype(new Genotype("1230"));
        assertNotEquals(
                new Agent(agent.getGenotype(), agent.getEnergy(), operator),
                agent.mutate(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrossOverEnergy() {
        agent.setEnergy(50);
        agent.cross(new Agent(new Genotype("0984324"), 10, null));
    }

    @Test
    public void testCrossOver() {
        agent.setGenotype(new Genotype("123445665432432524566857634"));
        agent.setEnergy(50);
        assertNotEquals(agent, agent.cross(new Agent(new Genotype("0984324"), 30, null)));
=======
    public void testMutation(){
        Operator operator = new Operator();
        agent.setGenotype(new Genotype("1230"));
        assertEquals(agent, operator.mutate(agent, 100));
    }

    @Test
    public void testCrossing(){
        Operator operator = new Operator();
        agent.setGenotype(new Genotype("123445665432432524566857634"));
        agent.setEnergy(50);
        assertNotEquals(agent, operator.copulate(agent, new Agent(new Genotype("0984324"), 10, null)));
>>>>>>> fix tests
    }
}
