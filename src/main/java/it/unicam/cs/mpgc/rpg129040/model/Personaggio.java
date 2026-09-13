
package it.unicam.cs.mpgc.rpg129040.model;

public abstract class Personaggio {
    private String nome;
    private int livello;
    private int hpMax;
    private int hpAttuali;
    private int dannoBase;

    public Personaggio(String nome, int livello, int hpMax, int dannoBase) {
        this.nome = nome;
        this.livello = livello;
        this.hpMax = hpMax;
        this.hpAttuali = hpMax;
        this.dannoBase = dannoBase;
    }

    public boolean isVivo() {
        return hpAttuali > 0;
    }

    // Getter e Setter
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getLivello() { return livello; }
    public void setLivello(int livello) { this.livello = livello; }

    public int getHpMax() { return hpMax; }
    public void setHpMax(int hpMax) { this.hpMax = hpMax; }

    public int getHpAttuali() { return hpAttuali; }
    public void setHpAttuali(int hpAttuali) {
        this.hpAttuali = Math.clamp(hpAttuali, 0, hpMax);
    }

    public int getDannoBase() { return dannoBase; }
    public void setDannoBase(int dannoBase) { this.dannoBase = dannoBase; }
}