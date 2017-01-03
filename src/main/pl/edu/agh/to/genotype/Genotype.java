package pl.edu.agh.to.genotype;

import java.util.List;

public class Genotype<T> {

    private final List<T> genotyp;

    public Genotype(List<T> genotyp) {
        this.genotyp = genotyp;
    }

    public List<T> get() {
        return genotyp;
    }
}