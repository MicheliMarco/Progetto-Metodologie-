
package it.unicam.cs.mpgc.rpg129040.model;

import java.util.List;

public record ZoneConfig(
        int id,
        String nome,
        int livelloBase,
        String sfondoPath,
        boolean eCastelloBoss,
        double buttonX,
        double buttonY,
        List<EnemyConfig> nemici
) {}