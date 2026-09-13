
package it.unicam.cs.mpgc.rpg129040.model;

public class Nemico extends Personaggio {
    private String texturePath;
    private double larghezzaTexture;
    private boolean eFase2 = false;
    private int turniFuocoRimanenti = 0;
    private int dannoFuocoPerTurno = 0;

    public Nemico(String nome, int livello, String texturePath, int hpBase, int dannoBase, double moltDifficolta, double larghezzaTexture) {
        super(
                nome,
                livello,
                (int) (hpBase * (1 + (livello - 1) * 0.15) * moltDifficolta),
                (int) (dannoBase * (1 + (livello - 1) * 0.12) * moltDifficolta)
        );
        this.texturePath = texturePath;
        this.larghezzaTexture = larghezzaTexture;
    }

    public void trasformaInFase2() {
        if (!eFase2) {
            this.eFase2 = true;
            setNome(getNome() + " (Fase 2 - Furia)");
            setHpMax((int) (getHpMax() * 1.5));
            setHpAttuali(getHpMax());
            setDannoBase((int) (getDannoBase() * 1.4));
            this.texturePath = "nemico9_fase2.png"; // Cambia texture dinamicamente
            this.larghezzaTexture = 260;
        }
    }

    public int getDanno() {
        return getDannoBase();
    }
    public void applicaFuoco(int dannoFuoco) {
        this.turniFuocoRimanenti = 2;
        this.dannoFuocoPerTurno = dannoFuoco;
    }

    public int processaDannoFuocoAEndTurno() {
        if (turniFuocoRimanenti > 0 && isVivo()) {
            turniFuocoRimanenti--;
            setHpAttuali(getHpAttuali() - dannoFuocoPerTurno);
            return dannoFuocoPerTurno;
        }
        return 0;
    }

    public String getTexturePath() { return texturePath; }
    public double getLarghezzaTexture() { return larghezzaTexture; }
    public boolean iseFase2() { return eFase2; }
}