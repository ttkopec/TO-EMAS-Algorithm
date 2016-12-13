package pl.edu.agh.to.agent;

import pl.edu.agh.to.operators.Operators;

public class AgentConfig {
    private final double reproductionEnergy;
    private final double startEnergy;
    private final double deathEnergy;
    private final Operators operator;

    public AgentConfig(double reproductionEnergy, double startEnergy, double deathEnergy, Operators operator) {
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

    public Operators getOperators() {
        return operator;
    }
}
