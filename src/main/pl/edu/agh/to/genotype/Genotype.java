package pl.edu.agh.to.genotype;

public class Genotype {
    public String getGenotyp() {
        return genotyp;
    }

    private final String genotyp;

    public Genotype(String genotyp) {
        this.genotyp = genotyp;
    }
    public String toString(){
        return genotyp;
    }
}