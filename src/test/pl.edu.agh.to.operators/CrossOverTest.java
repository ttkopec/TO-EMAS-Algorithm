package pl.edu.agh.to.operators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.genotype.Genotype;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CrossOverTest {
    private AgentConfig agentConfig = new AgentConfig(50, 200, 0);
    private Agent firstAgent;
    private Agent secondAgent;
    private CrossOverOperator crossOverOperator = new CrossOverOperator();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void beforeEach() {
        firstAgent = new Agent(null, 0, agentConfig);
        secondAgent = new Agent(null, 0, agentConfig);
    }

    @Test
    public void testArguments() {
        assertFalse("Incorrect number of arguments: 0", crossOverOperator.checkTypes());
        assertFalse("Incorrect number of arguments: 4", crossOverOperator.checkTypes(firstAgent, secondAgent, 1, null));
        assertFalse("First argument not an instance of Agent", crossOverOperator.checkTypes(1, secondAgent, 1));
        assertFalse("Second argument not an instance of Agent", crossOverOperator.checkTypes(firstAgent, 1, secondAgent));
        assertFalse("Third argument not an instance of Integer", crossOverOperator.checkTypes(firstAgent, secondAgent, null));
        firstAgent.setGenotype(new Genotype(new ArrayList<>()));
        secondAgent.setGenotype(new Genotype(new ArrayList<>()));
        assertTrue(crossOverOperator.checkTypes(firstAgent, secondAgent, 1));

        // TODO test genotype
    }

    @Test
    public void testExecution() {
        Random random = new Random();
        int genotypeSize = random.nextInt(91) + 10;
        ArrayList<Double> firstGenotype = new ArrayList<>(genotypeSize);
        ArrayList<Double> secondGenotype = new ArrayList<>(genotypeSize);
        for (int i = 0; i < genotypeSize; i++) {
            firstGenotype.add(random.nextDouble());
            secondGenotype.add(random.nextDouble());
        }
        firstAgent.setGenotype(new Genotype(firstGenotype));
        secondAgent.setGenotype(new Genotype(secondGenotype));

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parents don't have enough energy to support child");
        crossOverOperator.execute(firstAgent, secondAgent, 120);

        assertNotEquals("New Agent should differ from first one", firstAgent,
                crossOverOperator.execute(firstAgent, secondAgent, 100));
        assertNotEquals("New Agent should differ from second one", secondAgent,
                crossOverOperator.execute(firstAgent, secondAgent, 100));
    }

}
