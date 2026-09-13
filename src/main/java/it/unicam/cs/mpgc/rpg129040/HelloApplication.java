package it.unicam.cs.mpgc.rpg129040;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;

import it.unicam.cs.mpgc.rpg129040.model.Difficilita;
import it.unicam.cs.mpgc.rpg129040.model.EnemyConfig;
import it.unicam.cs.mpgc.rpg129040.model.Eroe;
import it.unicam.cs.mpgc.rpg129040.model.Nemico;
import it.unicam.cs.mpgc.rpg129040.model.Statistica;
import it.unicam.cs.mpgc.rpg129040.model.ZoneConfig;
import it.unicam.cs.mpgc.rpg129040.repository.IZoneRepository;
import it.unicam.cs.mpgc.rpg129040.repository.ZoneRepository;
import it.unicam.cs.mpgc.rpg129040.service.BattleManager;
import it.unicam.cs.mpgc.rpg129040.service.ZoneManager;
import it.unicam.cs.mpgc.rpg129040.ui.GameUIManager;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class HelloApplication extends GameApplication {

    private Eroe eroe;
    private ZoneManager zoneManager;
    private BattleManager battleManager;
    private GameUIManager uiManager;

    private Entity sfondoEntity;
    private Entity eroeEntity;

    private final List<Entity> enemyEntities = new ArrayList<>();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("RPG Monster Assault");
        settings.setVersion("2.0");
        settings.setMainMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.noneOf(MenuItem.class));
    }

    @Override
    protected void initGame() {
        IZoneRepository repository = new ZoneRepository();
        zoneManager = new ZoneManager(repository);

        eroe = new Eroe("Cavaliere", 100, 15, 10, 5, 3);
        battleManager = new BattleManager(eroe);
        uiManager = new GameUIManager();

        impostaDifficoltaENuovaOndata(Difficilita.BASE);
    }

    @Override
    protected void initUI() {
        uiManager.initTextNodes();

        addUINode(uiManager.getUiEroeStats(), 20, 30);
        addUINode(uiManager.getUiAzioniText(), 20, 75);

        VBox boxEroe = uiManager.creaBarreVisiveEroe();
        addUINode(boxEroe, 50, 165);

        VBox[] barreNemici = uiManager.creaBarreVisiveNemici();
        addUINode(barreNemici[0], 430, 185);
        addUINode(barreNemici[1], 610, 185);

        VBox pannelloAzioni = uiManager.creaPannelloAzioneInBasso(
                this::eseguiAttaccoSingolo1,
                this::eseguiAttaccoSingolo2,
                this::eseguiAttaccoArea,
                this::eseguiCura
        );
        addUINode(pannelloAzioni, 100, 440);

        Pane menuMappa = uiManager.creaPannelloSelezioneZonaConMappa(
                zoneManager.getTutteLeZone(),
                this::selezionaZonaDaMappa
        );
        addUINode(menuMappa, 20, 80);

        VBox menuDiff = uiManager.creaPannelloSelezioneDifficolta(this::avviaProssimaOndata);
        addUINode(menuDiff, 175, 150);

        VBox menuStats = uiManager.creaPannelloStatistiche(
                this::allocaPuntoStatistica,
                () -> {
                    if (eroe.potenziAttaccoSingolo()) uiManager.aggiornaGraficaUI(battleManager, zoneManager);
                },
                () -> {
                    if (eroe.potenziAttaccoArea()) uiManager.aggiornaGraficaUI(battleManager, zoneManager);
                }
        );
        addUINode(menuStats, 180, 110);

        uiManager.aggiornaStatoBottoniZone(zoneManager.getTutteLeZone(), zoneManager.getLivelloZonaMassimoSbloccato());
        uiManager.aggiornaGraficaUI(battleManager, zoneManager);

        HBox menuTopRight = uiManager.creaMenuTopRight(
                () -> {
                    if (battleManager.isPlayerTurn() && eroe.isVivo()) {
                        uiManager.aggiornaStatoBottoniZone(zoneManager.getTutteLeZone(), zoneManager.getLivelloZonaMassimoSbloccato());
                        uiManager.mostraMenuZone(!uiManager.isQualsiasiMenuVisibile());
                    }
                },
                () -> {
                    if (eroe.isVivo()) {
                        uiManager.toggleMenuStatistiche();
                        uiManager.aggiornaGraficaUI(battleManager, zoneManager);
                    }
                }
        );

        addUINode(menuTopRight, 580, 20);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Apri Menu Zone") {
            @Override
            protected void onActionBegin() {
                if (battleManager.isPlayerTurn() && eroe.isVivo()) {
                    uiManager.aggiornaStatoBottoniZone(zoneManager.getTutteLeZone(), zoneManager.getLivelloZonaMassimoSbloccato());
                    uiManager.mostraMenuZone(!uiManager.isQualsiasiMenuVisibile());
                }
            }
        }, KeyCode.M);

        getInput().addAction(new UserAction("Apri Menu Statistiche") {
            @Override
            protected void onActionBegin() {
                if (eroe.isVivo()) {
                    uiManager.toggleMenuStatistiche();
                    uiManager.aggiornaGraficaUI(battleManager, zoneManager);
                }
            }
        }, KeyCode.C);
    }

    private void selezionaZonaDaMappa(int idZona) {
        if (zoneManager.cambiaZona(idZona)) {
            uiManager.mostraMenuZone(false);
            uiManager.mostraMenuDifficolta(true);
        }
    }

    private void avviaProssimaOndata(Difficilita diff) {
        uiManager.mostraMenuDifficolta(false);
        zoneManager.incrementaOndata();
        eroe.aggiungiPuntiBonus(2);
        impostaDifficoltaENuovaOndata(diff);
        getNotificationService().pushNotification("Ondata " + zoneManager.getOndataCorrente() + " in " + zoneManager.getZonaCorrente().nome() + " (" + diff.getEtichetta() + ")!");
    }

    private void impostaDifficoltaENuovaOndata(Difficilita diff) {
        ZoneConfig config = zoneManager.getZonaCorrente();
        battleManager.preparaNuovaOndata(config, diff);

        spawnAmbiente(config.sfondoPath());
        spawnEntitaBattaglia(config.nemici());
        uiManager.aggiornaGraficaUI(battleManager, zoneManager);
    }

    private void spawnAmbiente(String sfondoPath) {
        if (sfondoEntity != null) sfondoEntity.removeFromWorld();

        Texture sfondo = texture(sfondoPath);
        sfondo.setFitWidth(800);
        sfondo.setFitHeight(600);

        sfondoEntity = entityBuilder()
                .at(0, 0)
                .view(sfondo)
                .zIndex(-100)
                .buildAndAttach();
    }

    private void spawnEntitaBattaglia(List<EnemyConfig> nemiciConfig) {
        if (eroeEntity != null) eroeEntity.removeFromWorld();

        enemyEntities.forEach(Entity::removeFromWorld);
        enemyEntities.clear();

        eroeEntity = entityBuilder()
                .at(80, 280)
                .viewWithBBox("eroe.png")
                .buildAndAttach();

        for (EnemyConfig cfg : nemiciConfig) {
            Texture t = texture(cfg.texturePath());
            t.setFitWidth(cfg.larghezzaTexture());
            t.setPreserveRatio(true);

            Entity e = entityBuilder()
                    .at(cfg.spawnX(), cfg.spawnY())
                    .viewWithBBox(t)
                    .buildAndAttach();

            enemyEntities.add(e);
        }
    }

    private void eseguiAttaccoSingolo1() {
        if (!puoiAgire() || battleManager.getNemiciCorrenti().isEmpty()) return;
        eseguiColpoSuNemico(0);
    }

    private void eseguiAttaccoSingolo2() {
        if (!puoiAgire() || battleManager.getNemiciCorrenti().size() < 2) return;
        eseguiColpoSuNemico(1);
    }

    private void eseguiColpoSuNemico(int index) {
        Nemico nemico = battleManager.getNemiciCorrenti().get(index);
        if (!nemico.isVivo()) return;

        Entity targetEntity = index < enemyEntities.size() ? enemyEntities.get(index) : null;

        int danno = eroe.calcolaDannoSingoloEffettivo(nemico);
        nemico.setHpAttuali(nemico.getHpAttuali() - danno);

        if (eroe.getLvlAttaccoSingolo() >= 1) {
            nemico.applicaFuoco((int) (danno * 0.20));
        }

        uiManager.mostraTestoDanno(danno, targetEntity != null ? targetEntity.getPosition() : new Point2D(500, 300), Color.RED);
        if (targetEntity != null) uiManager.animaAttacco(eroeEntity, targetEntity);
        if (!nemico.isVivo() && targetEntity != null) targetEntity.removeFromWorld();

        consumaAzione();
    }

    private void eseguiAttaccoArea() {
        if (!puoiAgire()) return;

        eseguiSingolaOndataArea();

        if (eroe.getLvlAttaccoArea() >= 2 && Math.random() < 0.5) {
            getNotificationService().pushNotification("⚡ ATTACCO AD AREA DOPPIO!");
            getEngineTimer().runOnceAfter(this::eseguiSingolaOndataArea, Duration.seconds(0.3));
        }

        consumaAzione();
    }

    private void eseguiSingolaOndataArea() {
        int dannoArea = eroe.calcolaDannoArea();
        List<Nemico> nemici = battleManager.getNemiciCorrenti();
        int dannoTotaleInflitto = 0;

        for (int i = 0; i < nemici.size(); i++) {
            Nemico n = nemici.get(i);
            if (n.isVivo()) {
                n.setHpAttuali(n.getHpAttuali() - dannoArea);
                dannoTotaleInflitto += dannoArea;

                if (eroe.getLvlAttaccoArea() >= 1) {
                    n.applicaFuoco((int) (dannoArea * 0.20));
                }

                Entity eTarget = i < enemyEntities.size() ? enemyEntities.get(i) : null;
                uiManager.mostraTestoDanno(dannoArea, eTarget != null ? eTarget.getPosition() : new Point2D(500, 300), Color.ORANGE);
                if (!n.isVivo() && eTarget != null) eTarget.removeFromWorld();
            }
        }

        if (eroe.getLvlAttaccoArea() >= 3 && dannoTotaleInflitto > 0) {
            int cura = (int) (dannoTotaleInflitto * 0.50);
            eroe.setHpAttuali(eroe.getHpAttuali() + cura);
            uiManager.mostraTestoDanno(cura, eroeEntity.getPosition(), Color.GREEN);
        }

        if (eroeEntity != null && !enemyEntities.isEmpty()) {
            uiManager.animaAttacco(eroeEntity, enemyEntities.get(0));
        }
    }

    private void eseguiCura() {
        if (!puoiAgire()) return;

        if (battleManager.getCureRimanenti() <= 0) {
            getNotificationService().pushNotification("Cure esaurite per questa ondata!");
            return;
        }

        battleManager.consumaCura();
        int cura = (int) (eroe.getHpMax() * 0.4);
        eroe.setHpAttuali(eroe.getHpAttuali() + cura);
        uiManager.mostraTestoDanno(cura, eroeEntity.getPosition(), Color.GREEN);

        consumaAzione();
    }

    private void consumaAzione() {
        battleManager.consumaAzione();

        if (battleManager.controllaENotificaTrasformazioneBoss()) {
            getNotificationService().pushNotification("⚠️ IL BOSS SI È TRASFORMATO! FASE 2!");
            if (!enemyEntities.isEmpty() && enemyEntities.get(0) != null) {
                enemyEntities.get(0).removeFromWorld();
            }

            Nemico boss = battleManager.getNemiciCorrenti().get(0);
            Texture tFase2 = texture(boss.getTexturePath());
            tFase2.setFitWidth(boss.getLarghezzaTexture());
            tFase2.setPreserveRatio(true);

            Entity bossEntity = entityBuilder()
                    .at(480, 240)
                    .viewWithBBox(tFase2)
                    .buildAndAttach();

            if (!enemyEntities.isEmpty()) {
                enemyEntities.set(0, bossEntity);
            } else {
                enemyEntities.add(bossEntity);
            }
        }

        uiManager.aggiornaGraficaUI(battleManager, zoneManager);

        if (battleManager.sonoTuttiINemiciMorti()) {
            if (battleManager.controllaESbloccaPuntoAbilita(zoneManager.getZonaCorrente().id(), battleManager.getDifficoltaCorrente())) {
                getNotificationService().pushNotification("🌟 PRIMA VITTORIA DIFFICILE! +1 PUNTO ABILITÀ!");
            }

            if (zoneManager.verificaSbloccoNuovaZona(battleManager.getDifficoltaCorrente())) {
                getNotificationService().pushNotification("🔓 NUOVA ZONA SBLOCCATA!");
            }
            uiManager.mostraMenuDifficolta(true);
            return;
        }

        if (battleManager.getAzioniRimaste() <= 0) {
            eseguiTurnoNemico();
        }
    }

    private void eseguiTurnoNemico() {
        battleManager.setPlayerTurn(false);
        uiManager.aggiornaGraficaUI(battleManager, zoneManager);

        getEngineTimer().runOnceAfter(() -> {
            if (!enemyEntities.isEmpty() && enemyEntities.get(0) != null && enemyEntities.get(0).isActive()) {
                uiManager.animaAttacco(enemyEntities.get(0), eroeEntity);
            }

            int dannoSubito = battleManager.calcolaSubitoDannoNemici();
            uiManager.mostraTestoDanno(dannoSubito, eroeEntity.getPosition(), Color.PURPLE);

            if (!eroe.isVivo()) {
                getNotificationService().pushNotification("☠ SEI STATO SCONFITTO!");

                VBox panelGameOver = uiManager.creaPannelloGameOver(() -> {
                    getGameController().gotoMainMenu();
                });
                addUINode(panelGameOver, 200, 200);

            } else {
                battleManager.resetAzioni();
                battleManager.setPlayerTurn(true);
            }
            uiManager.aggiornaGraficaUI(battleManager, zoneManager);
        }, Duration.seconds(1.2));
    }

    private void allocaPuntoStatistica(Statistica stat) {
        if (eroe.allocaPunto(stat)) {
            uiManager.aggiornaGraficaUI(battleManager, zoneManager);
        } else {
            getNotificationService().pushNotification("Punti insufficienti!");
        }
    }

    private boolean puoiAgire() {
        return battleManager.isPlayerTurn() &&
                battleManager.getAzioniRimaste() > 0 &&
                eroe.isVivo() &&
                !battleManager.sonoTuttiINemiciMorti() &&
                !uiManager.isQualsiasiMenuVisibile();
    }

    public static void main(String[] args) {
        launch(args);
    }
}