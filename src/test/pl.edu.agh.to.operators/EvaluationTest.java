package pl.edu.agh.to.operators;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.genotype.Genotype;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EvaluationTest {
    private AgentConfig agentConfig = new AgentConfig(50, 200, 0);
    private Agent agent;
    private EvaluationOperator evaluationOperator = new EvaluationOperator();

    @Before
    public void beforeEach() {
        agent = new Agent(null, 0, agentConfig);
    }

    @Test
    public void testArguments() {
        agent.setGenotype(new Genotype(new ArrayList<>()));

        assertFalse("Incorrect number of arguments: 0", evaluationOperator.checkTypes());
        assertFalse("Incorrect number of arguments: 2", evaluationOperator.checkTypes(agent, null));
        assertFalse("Argument not an instance of Agent", evaluationOperator.checkTypes(new Object()));

        // TODO test genotype (currently it's hardcoded List<Double>, should be generic)
    }

    @Test
    public void testExecution() {
        ArrayList<Double> genotype = new ArrayList<>();
        genotype.add(0d);
        genotype.add(1d);
        genotype.add(2d);
        agent.setGenotype(new Genotype(genotype));

        assertEquals(5d, evaluationOperator.execute(agent));

        genotype.clear();
        genotype.add(1.513);
        genotype.add(-5.12);
        genotype.add(2.314);

        assertEquals(70.4488746744982, evaluationOperator.execute(agent));
    }
}
