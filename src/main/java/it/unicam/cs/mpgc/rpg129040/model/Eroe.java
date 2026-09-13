package it.unicam.cs.mpgc.rpg129040.model;

public class Eroe extends Personaggio {
    private int forza;
    private int difesa;
    private int scudoMax;
    private int scudoAttuale;
    private int puntiStatBonus;
    private int puntiAbilita = 0;
    private int lvlAttaccoSingolo = 0; // max 3
    private int lvlAttaccoArea = 0;    // max 3

    public Eroe(String nome, int hpMax, int dannoBase, int forza, int difesa, int puntiBonusIniziali) {
        super(nome, 1, hpMax, dannoBase);
        this.forza = forza;
        this.difesa = difesa;
        this.scudoMax = difesa * 3;
        this.scudoAttuale = scudoMax;
        this.puntiStatBonus = puntiBonusIniziali;
    }

    public int calcolaDannoSingolo() {
        return getDannoBase() + (int) (forza * 1.8);
    }

    public int calcolaDannoArea() {
        return (int) ((getDannoBase() + (forza * 1.2)) * 0.7);
    }

    public void ripristinaScudo() {
        this.scudoMax = difesa * 3;
        this.scudoAttuale = scudoMax;
    }

    public void aggiungiPuntiBonus(int quantita) {
        this.puntiStatBonus += quantita;
    }

    public boolean allocaPunto(Statistica stat) {
        if (puntiStatBonus <= 0 || stat == null) return false;

        switch (stat) {
            case FORZA -> forza += 3;
            case HP -> {
                setHpMax(getHpMax() + 25);
                setHpAttuali(getHpAttuali() + 25);
            }
            case DIFESA -> {
                difesa += 2;
                ripristinaScudo();
            }
        }

        puntiStatBonus--;
        return true;
    }

    public int getPuntiAbilita() { return puntiAbilita; }
    public void aggiungiPuntiAbilita(int punti) { this.puntiAbilita += punti; }

    public int getLvlAttaccoSingolo() { return lvlAttaccoSingolo; }
    public int getLvlAttaccoArea() { return lvlAttaccoArea; }

    public boolean potenziAttaccoSingolo() {
        if (puntiAbilita > 0 && lvlAttaccoSingolo < 3) {
            lvlAttaccoSingolo++;
            puntiAbilita--;
            return true;
        }
        return false;
    }

    public boolean potenziAttaccoArea() {
        if (puntiAbilita > 0 && lvlAttaccoArea < 3) {
            lvlAttaccoArea++;
            puntiAbilita--;
            return true;
        }
        return false;
    }

    // Calcolo del danno singolo potenziato
    public int calcolaDannoSingoloEffettivo(Nemico target) {
        int dannoBase = calcolaDannoSingolo();

        // Lvl 3: Uccisione istantanea sotto il 15% di vita
        if (lvlAttaccoSingolo >= 3 && target != null && target.isVivo()) {
            double percentualeHp = (double) target.getHpAttuali() / target.getHpMax();
            if (percentualeHp <= 0.15) {
                return target.getHpAttuali(); // Infligge tutti i punti vita rimasti
            }
        }

        // Lvl 2: 50% di probabilità di fare il 175% del danno (Critico)
        if (lvlAttaccoSingolo >= 2 && Math.random() < 0.5) {
            dannoBase = (int) (dannoBase * 1.75);
        }

        return dannoBase;
    }

    // Getter e Setter
    public int getForza() { return forza; }
    public int getDifesa() { return difesa; }
    public int getScudoMax() { return scudoMax; }
    public int getScudoAttuale() { return scudoAttuale; }
    public void setScudoAttuale(int scudoAttuale) {
        this.scudoAttuale = Math.clamp(scudoAttuale, 0, scudoMax);
    }
    public int getPuntiStatBonus() { return puntiStatBonus; }
}