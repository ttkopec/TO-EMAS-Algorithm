package pl.edu.agh.to.operators;

import org.junit.Test;
import pl.edu.agh.to.agent.Agent;
import pl.edu.agh.to.agent.AgentConfig;
import pl.edu.agh.to.core.util.OperatorsConfig;
import pl.edu.agh.to.core.util.TypedOperatorsConfig;
import pl.edu.agh.to.genotype.Genotype;
import scala.Option;
import scala.reflect.internal.util.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class OperatorTest {
    private AgentConfig config = new AgentConfig(50, 200, 0);
    private OperatorsConfig operatorsConfig = new OperatorsConfig() {

        @Override
        public Operator crossOverOperator() {
            return new CrossOverOperator();
        }

        @Override
        public Operator mutationOperator() {
            return new MutationOperator();
        }

        @Override
        public Operator selectionOperator() {
            return new SelectionOperator();
        }

        @Override
        public Operator evaluationOperator() {
            return new EvaluationOperator();
        }
    };
    private Agent agent = new Agent(null, 0, config);

    @Test
    public void testSelection(){
        agent.setEnergy(-1.0);
        assertFalse(agent.isAlive());

        agent.setEnergy(1.0);
        assertTrue(agent.isAlive());
    }

    @Test
    public void testMutation(){
        Random random = new Random();
        List<Double> genotypeExample = new ArrayList<>(10);
        for (int i=0; i<10; ++i) {
            genotypeExample.add(random.nextDouble());
        }
        agent.setGenotype(new Genotype(genotypeExample));
        assertNotEquals(agent, operatorsConfig.mutationOperator().execute(agent));
    }

    @Test
    public void testCrossing(){
        Random random = new Random();
        List<Double> genotypeExample = new ArrayList<>(10);
        List<Double> secondGenotypeExample = new ArrayList<>(10);
        for (int i=0; i<10; ++i) {
            genotypeExample.add(random.nextDouble());
            secondGenotypeExample.add(random.nextDouble());
        }
        agent.setGenotype(new Genotype(genotypeExample));
        agent.setEnergy(100);


        Agent secondAgent = new Agent(new Genotype(secondGenotypeExample), 100, config);
        assertNotEquals(agent, operatorsConfig.crossOverOperator().execute(agent, secondAgent));
    }
}
