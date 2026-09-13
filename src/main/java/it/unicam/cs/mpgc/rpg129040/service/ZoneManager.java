
package it.unicam.cs.mpgc.rpg129040.service;

import it.unicam.cs.mpgc.rpg129040.model.Difficilita;
import it.unicam.cs.mpgc.rpg129040.model.ZoneConfig;
import it.unicam.cs.mpgc.rpg129040.repository.IZoneRepository;

import java.util.List;

public class ZoneManager {
    private final IZoneRepository repository;
    private ZoneConfig zonaCorrente;
    private int ondataCorrente = 1;
    private int livelloZonaMassimoSbloccato = 1;

    public ZoneManager(IZoneRepository repository) {
        this.repository = repository;
        this.zonaCorrente = repository.getZonaById(1);
    }

    public boolean cambiaZona(int idZona) {
        if (idZona <= livelloZonaMassimoSbloccato) {
            this.zonaCorrente = repository.getZonaById(idZona);
            this.ondataCorrente = 1;
            return true;
        }
        return false;
    }

    public boolean verificaSbloccoNuovaZona(Difficilita difficolta) {
        if (Difficilita.DIFFICILE.equals(difficolta) && zonaCorrente.id() == livelloZonaMassimoSbloccato) {
            if (livelloZonaMassimoSbloccato < repository.getTutteLeZone().size()) {
                livelloZonaMassimoSbloccato++;
                return true;
            }
        }
        return false;
    }

    public void incrementaOndata() {
        this.ondataCorrente++;
    }

    // Getter
    public ZoneConfig getZonaCorrente() { return zonaCorrente; }
    public int getOndataCorrente() { return ondataCorrente; }
    public int getLivelloZonaMassimoSbloccato() { return livelloZonaMassimoSbloccato; }
    public List<ZoneConfig> getTutteLeZone() { return repository.getTutteLeZone(); }
}