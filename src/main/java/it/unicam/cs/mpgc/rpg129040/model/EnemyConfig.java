
package it.unicam.cs.mpgc.rpg129040.model;

public record EnemyConfig(
        String nome,
        int livello,
        String texturePath,
        int hpBase,
        int dannoBase,
        double larghezzaTexture,
        double spawnX,
        double spawnY
) {}