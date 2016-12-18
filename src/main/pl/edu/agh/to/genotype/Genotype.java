package pl.edu.agh.to.genotype;

import java.util.List;

public class Genotype {

    private final List<Double> genotyp;

    public Object getGenotyp() {
        return genotyp;
    }

    public Genotype(Object genotyp) {
        this.genotyp = (List)genotyp;
    }

}