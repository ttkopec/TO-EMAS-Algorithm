package pl.edu.agh.to.agent;

import pl.edu.agh.to.operators.Operator;

public class AgentConfig {
    private final double reproductionEnergy;
    private final double startEnergy;
    private final double deathEnergy;
    private final Operator operator;

    public AgentConfig(double reproductionEnergy, double startEnergy, double deathEnergy, Operator operator) {
        this.startEnergy = startEnergy;
        this.reproductionEnergy = reproductionEnergy;
        this.deathEnergy = deathEnergy;
        this.operator = operator;
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

    public Operator getOperator() {
        return operator;
    }
}
