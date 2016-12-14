package pl.edu.agh.to.operators;

import org.junit.Test;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.genotype.Genotype;

import static org.junit.Assert.*;

/**
 * Created by tkopec on 04.12.16.
 */
public class OperatorTest {
    private Agent agent = new Agent(null, 0, new Operator());

    @Test(expected = NumberFormatException.class)
    public void testEvaluation() {
        agent.setGenotype(new Genotype("numberFormatException"));
        agent.getFitness();
    }

    @Test
    public void testSelection(){
        agent.setEnergy(-1.0);
        assertFalse(agent.isAlive());

        agent.setEnergy(1.0);
        assertTrue(agent.isAlive());
    }

    @Test
    public void testMutation(){
        agent.setGenotype(new Genotype("1230"));
        assertNotEquals(agent, agent.mutate(10));



    }

    @Test
    public void testCrossing(){
        agent.setGenotype(new Genotype("123445665432432524566857634"));
        agent.setEnergy(50);
        assertNotEquals(agent, agent.cross(new Agent(new Genotype("0984324"), 10, null)));
    }
}
