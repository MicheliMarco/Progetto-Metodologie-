
package it.unicam.cs.mpgc.rpg129040.model;

public enum Difficilita {
    BASE("Base", 1.0),
    MEDIA("Media", 1.5),
    DIFFICILE("Difficile", 2.0);

    private final String etichetta;
    private final double moltiplicatore;

    Difficilita(String etichetta, double moltiplicatore) {
        this.etichetta = etichetta;
        this.moltiplicatore = moltiplicatore;
    }

    public String getEtichetta() { return etichetta; }
    public double getMoltiplicatore() { return moltiplicatore; }
}