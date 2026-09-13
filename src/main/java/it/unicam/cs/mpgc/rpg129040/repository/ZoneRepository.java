
package it.unicam.cs.mpgc.rpg129040.repository;

import it.unicam.cs.mpgc.rpg129040.model.EnemyConfig;
import it.unicam.cs.mpgc.rpg129040.model.ZoneConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneRepository implements IZoneRepository {
    private final Map<Integer, ZoneConfig> zoneMap = new HashMap<>();

    public ZoneRepository() {
        caricaZoneDefault();
    }

    private void caricaZoneDefault() {
        zoneMap.put(1, new ZoneConfig(1, "Foresta Oscura", 1, "sfondo_foresta.jpg", false, 50, 360, List.of(
                new EnemyConfig("Nemico 1", 1, "nemico1.png", 50, 12, 110, 450, 260),
                new EnemyConfig("Nemico 2", 2, "nemico2.png", 65, 15, 110, 600, 260)
        )));
        zoneMap.put(2, new ZoneConfig(2, "Entrata Caverna", 6, "sfondo_caverna_entrata.jpg", false, 280, 285, List.of(
                new EnemyConfig("Nemico 3", 6, "nemico3.png", 90, 18, 110, 450, 260),
                new EnemyConfig("Nemico 4", 7, "nemico4.png", 110, 22, 110, 600, 260)
        )));
        zoneMap.put(3, new ZoneConfig(3, "Caverna Profonda", 11, "sfondo_caverna_profonda.jpg", false, 480, 360, List.of(
                new EnemyConfig("Nemico 5", 11, "nemico5.png", 150, 28, 110, 400, 280),
                new EnemyConfig("Nemico 6", 12, "nemico6.png", 180, 34, 110, 610, 230)
        )));
        zoneMap.put(4, new ZoneConfig(4, "Vulcano", 16, "sfondo_vulcano.jpg", false, 560, 180, List.of(
                new EnemyConfig("Nemico 7", 16, "nemico7.png", 230, 42, 110, 450, 260),
                new EnemyConfig("Nemico 8", 17, "nemico8.png", 270, 50, 110, 600, 260)
        )));
        zoneMap.put(5, new ZoneConfig(5, "Castello Re Demone", 21, "sfondo_castello.jpg", true, 90, 175, List.of(
                new EnemyConfig("Nemico 9 (Re Demone)", 21, "nemico9.png", 500, 65, 200, 400, 190)
        )));
    }

    @Override
    public ZoneConfig getZonaById(int id) {
        return zoneMap.getOrDefault(id, zoneMap.get(1));
    }

    @Override
    public List<ZoneConfig> getTutteLeZone() {
        return new ArrayList<>(zoneMap.values());
    }
}