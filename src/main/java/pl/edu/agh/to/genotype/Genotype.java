package pl.edu.agh.to.genotype;

import java.util.List;

public class Genotype {

    private final List<Double> genotyp;

    public Genotype(List<Double> genotyp) {
        this.genotyp = genotyp;
    }

    public List<Double> getGenotyp() {
        return genotyp;
    }
}