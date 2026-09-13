package it.unicam.cs.mpgc.rpg129040.repository;

import it.unicam.cs.mpgc.rpg129040.model.ZoneConfig;
import java.util.List;

public interface IZoneRepository {
    ZoneConfig getZonaById(int id);
    List<ZoneConfig> getTutteLeZone();
}
