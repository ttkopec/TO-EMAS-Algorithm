package pl.edu.agh.to.agent;

import pl.edu.agh.to.operators.Operator;

public class AgentConfig {
    private final double reproductionEnergy;
    private final double startEnergy;
    private final double deathEnergy;

    private Operator crossOverOp;
    private Operator evaluationOp;
    private Operator mutationOp;
    private Operator selectionOp;

    public AgentConfig(double reproductionEnergy, double startEnergy, double deathEnergy) {
        this.startEnergy = startEnergy;
        this.reproductionEnergy = reproductionEnergy;
        this.deathEnergy = deathEnergy;
    }

    public double getReproductionEnergy() {
        return reproductionEnergy;
    }

    public double getStartEnergy() {
        return startEnergy;
    }

    public double getDeathEnergy() {
        return deathEnergy;
    }

    public Operator getCrossOverOp() {
        return crossOverOp;
    }

    public Operator getEvaluationOp() {
        return evaluationOp;
    }

    public Operator getMutationOp() {
        return mutationOp;
    }

    public Operator getSelectionOp() {
        return selectionOp;
    }
}
