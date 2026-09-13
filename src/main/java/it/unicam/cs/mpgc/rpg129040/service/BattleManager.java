package it.unicam.cs.mpgc.rpg129040.service;

import it.unicam.cs.mpgc.rpg129040.model.Difficilita;
import it.unicam.cs.mpgc.rpg129040.model.Eroe;
import it.unicam.cs.mpgc.rpg129040.model.Nemico;
import it.unicam.cs.mpgc.rpg129040.model.ZoneConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BattleManager {
    private final Eroe eroe;
    private final List<Nemico> nemiciCorrenti = new ArrayList<>();

    private Difficilita difficoltaCorrente = Difficilita.BASE;

    private int azioniRimaste = 2;
    private final int MAX_AZIONI = 2;
    private int cureRimanenti = 3;
    private final int MAX_CURE = 3;
    private boolean isPlayerTurn = true;
    private boolean eFase2ReDemone = false;
    private boolean eCastelloBoss = false;
    private final Set<Integer> zoneCompletateADifficile = new HashSet<>();

    public BattleManager(Eroe eroe) {
        this.eroe = eroe;
    }

    public void preparaNuovaOndata(ZoneConfig zona, Difficilita difficolta) {
        this.difficoltaCorrente = difficolta;
        this.eCastelloBoss = zona.eCastelloBoss();
        this.cureRimanenti = MAX_CURE;
        this.eFase2ReDemone = false;
        this.eroe.ripristinaScudo();
        this.azioniRimaste = MAX_AZIONI;
        this.isPlayerTurn = true;

        this.nemiciCorrenti.clear();
        double mult = difficolta.getMoltiplicatore();

        zona.nemici().forEach(cfg ->
                nemiciCorrenti.add(new Nemico(cfg.nome(), cfg.livello(), cfg.texturePath(), cfg.hpBase(), cfg.dannoBase(), mult, cfg.larghezzaTexture()))
        );
    }

    public boolean controllaENotificaTrasformazioneBoss() {
        if (eCastelloBoss && !eFase2ReDemone && sonoTuttiINemiciMorti()) {
            this.eFase2ReDemone = true;
            Nemico boss = nemiciCorrenti.get(0);
            boss.trasformaInFase2();
            return true;
        }
        return false;
    }

    public int calcolaSubitoDannoNemici() {
        int dannoTotale = nemiciCorrenti.stream()
                .filter(Nemico::isVivo)
                .mapToInt(Nemico::getDanno)
                .sum();

        double riduzione = Math.min(0.75, eroe.getDifesa() * 0.015);
        int dannoRidotto = Math.max(1, (int) (dannoTotale * (1 - riduzione)));

        if (eroe.getScudoAttuale() > 0) {
            if (dannoRidotto <= eroe.getScudoAttuale()) {
                eroe.setScudoAttuale(eroe.getScudoAttuale() - dannoRidotto);
            } else {
                int penetrato = dannoRidotto - eroe.getScudoAttuale();
                eroe.setScudoAttuale(0);
                eroe.setHpAttuali(eroe.getHpAttuali() - penetrato);
            }
        } else {
            eroe.setHpAttuali(eroe.getHpAttuali() - dannoRidotto);
        }

        return dannoRidotto;
    }

    public boolean sonoTuttiINemiciMorti() {
        return nemiciCorrenti.stream().noneMatch(Nemico::isVivo);
    }

    public boolean controllaESbloccaPuntoAbilita(int idZona, Difficilita diff) {
        if (diff == Difficilita.DIFFICILE && !zoneCompletateADifficile.contains(idZona)) {
            zoneCompletateADifficile.add(idZona);
            eroe.aggiungiPuntiAbilita(1);
            return true;
        }
        return false;
    }

    // Getter e Setter
    public Eroe getEroe() { return eroe; }
    public List<Nemico> getNemiciCorrenti() { return nemiciCorrenti; }
    public Difficilita getDifficoltaCorrente() { return difficoltaCorrente; }
    public int getAzioniRimaste() { return azioniRimaste; }
    public void consumaAzione() { azioniRimaste--; }
    public void resetAzioni() { azioniRimaste = MAX_AZIONI; }
    public int getMAX_AZIONI() { return MAX_AZIONI; }
    public int getCureRimanenti() { return cureRimanenti; }
    public void consumaCura() { cureRimanenti--; }
    public int getMAX_CURE() { return MAX_CURE; }
    public boolean isPlayerTurn() { return isPlayerTurn; }
    public void setPlayerTurn(boolean playerTurn) { isPlayerTurn = playerTurn; }
}