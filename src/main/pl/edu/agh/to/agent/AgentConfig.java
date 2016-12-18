package pl.edu.agh.to.agent;


public class AgentConfig {
    private final double reproductionEnergy;
    private final double startEnergy;
    private final double deathEnergy;

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
}