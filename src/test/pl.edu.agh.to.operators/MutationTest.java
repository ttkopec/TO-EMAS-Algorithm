package pl.edu.agh.to.operators;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.genotype.Genotype;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class MutationTest {
    private AgentConfig agentConfig = new AgentConfig(50, 200, 0);
    private Agent agent;
    private MutationOperator mutationOperator = new MutationOperator();
    
    @Before
    public void beforeEach() {
        agent = new Agent(null, 0, agentConfig);
    }

    @Test
    public void testArguments() {
        assertFalse("Incorrect number of arguments: 0", mutationOperator.checkTypes());
        assertFalse("Incorrect number of arguments: 3", mutationOperator.checkTypes(agent, 1, null));
        assertFalse("First argument not an instance of Agent", mutationOperator.checkTypes(1, agent));
        assertFalse("Second argument not an instance of Integer", mutationOperator.checkTypes(agent, '1'));
        assertTrue(mutationOperator.checkTypes(agent, 1));
    }

    @Test
    public void testExecution() {
        Random random = new Random();
        int genotypeSize = random.nextInt(91) + 10;
        ArrayList<Double> genotype = new ArrayList<>(genotypeSize);
        for (int i = 0; i < genotypeSize; i++)
            genotype.add(random.nextDouble());
        agent.setGenotype(new Genotype(genotype));

        ArrayList<Double> oldGenotype = new ArrayList<>();
        oldGenotype.addAll(genotype);

        assertNotEquals("Genotypes after mutation should be different",
                oldGenotype,
                ((Agent) mutationOperator.execute(agent, genotypeSize)).getGenotype().getGenotyp());
    }
}
