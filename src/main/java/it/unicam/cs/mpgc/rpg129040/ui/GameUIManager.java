package it.unicam.cs.mpgc.rpg129040.ui;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import it.unicam.cs.mpgc.rpg129040.model.Difficilita;
import it.unicam.cs.mpgc.rpg129040.model.Eroe;
import it.unicam.cs.mpgc.rpg129040.model.Nemico;
import it.unicam.cs.mpgc.rpg129040.model.Statistica;
import it.unicam.cs.mpgc.rpg129040.model.ZoneConfig;
import it.unicam.cs.mpgc.rpg129040.service.BattleManager;
import it.unicam.cs.mpgc.rpg129040.service.ZoneManager;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GameUIManager {

    private Text uiEroeStats;
    private Text uiAzioniText;

    private ProgressBar barVitaEroe;
    private ProgressBar barScudoEroe;
    private Label lblTestoHpVisivo;
    private Label lblTestoScudoVisivo;

    private ProgressBar barVitaEnemy1;
    private Label lblTestoHpEnemy1;
    private ProgressBar barVitaEnemy2;
    private Label lblTestoHpEnemy2;

    private Label lblAzioniContatore;
    private Label lblCureContatore;
    private Button btnAttacco1;
    private Button btnAttacco2;

    private VBox menuStatisticheBox;
    private Label lblPuntiDisponibili;
    private Label lblStatForza;
    private Label lblStatHp;
    private Label lblStatDifesa;
    private Label lblAbilitaSingola;
    private Label lblAbilitaArea;

    private Pane menuZonaBox;
    private VBox menuDifficoltaBox;

    private final Map<Integer, Button> bottoniMappa = new HashMap<>();

    public Text getUiEroeStats() { return uiEroeStats; }
    public Text getUiAzioniText() { return uiAzioniText; }

    public void initTextNodes() {
        uiEroeStats = new Text();
        uiEroeStats.setFill(Color.WHITE);
        uiEroeStats.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        uiAzioniText = new Text();
        uiAzioniText.setFill(Color.YELLOW);
        uiAzioniText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    }

    public VBox creaBarreVisiveEroe() {
        VBox contenitore = new VBox(2);
        contenitore.setAlignment(Pos.CENTER);
        contenitore.setPrefWidth(140);

        lblTestoScudoVisivo = new Label();
        lblTestoScudoVisivo.setStyle("-fx-text-fill: #00BFFF; -fx-font-weight: bold; -fx-font-size: 11px;");

        barScudoEroe = new ProgressBar(1.0);
        barScudoEroe.setPrefWidth(130);
        barScudoEroe.setPrefHeight(10);
        barScudoEroe.setStyle("-fx-accent: #00BFFF;");

        lblTestoHpVisivo = new Label();
        lblTestoHpVisivo.setStyle("-fx-text-fill: #32CD32; -fx-font-weight: bold; -fx-font-size: 11px;");

        barVitaEroe = new ProgressBar(1.0);
        barVitaEroe.setPrefWidth(130);
        barVitaEroe.setPrefHeight(12);

        contenitore.getChildren().addAll(lblTestoScudoVisivo, barScudoEroe, lblTestoHpVisivo, barVitaEroe);
        return contenitore;
    }

    public VBox[] creaBarreVisiveNemici() {
        VBox box1 = new VBox(2);
        box1.setAlignment(Pos.CENTER);
        box1.setPrefWidth(140);
        lblTestoHpEnemy1 = new Label();
        lblTestoHpEnemy1.setStyle("-fx-text-fill: #FF4500; -fx-font-weight: bold; -fx-font-size: 11px;");
        barVitaEnemy1 = new ProgressBar(1.0);
        barVitaEnemy1.setPrefWidth(120);
        barVitaEnemy1.setPrefHeight(11);
        barVitaEnemy1.setStyle("-fx-accent: #FF4500;");
        box1.getChildren().addAll(lblTestoHpEnemy1, barVitaEnemy1);

        VBox box2 = new VBox(2);
        box2.setAlignment(Pos.CENTER);
        box2.setPrefWidth(140);
        lblTestoHpEnemy2 = new Label();
        lblTestoHpEnemy2.setStyle("-fx-text-fill: #FF4500; -fx-font-weight: bold; -fx-font-size: 11px;");
        barVitaEnemy2 = new ProgressBar(1.0);
        barVitaEnemy2.setPrefWidth(120);
        barVitaEnemy2.setPrefHeight(11);
        barVitaEnemy2.setStyle("-fx-accent: #FF4500;");
        box2.getChildren().addAll(lblTestoHpEnemy2, barVitaEnemy2);

        return new VBox[]{box1, box2};
    }

    public VBox creaPannelloAzioneInBasso(Runnable atk1, Runnable atk2, Runnable atkArea, Runnable cura) {
        VBox contenitore = new VBox(10);
        contenitore.setAlignment(Pos.CENTER);
        contenitore.setPadding(new Insets(12, 20, 12, 20));
        contenitore.setStyle("-fx-background-color: rgba(15, 15, 25, 0.9); -fx-border-color: #444; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        contenitore.setPrefWidth(600);

        HBox rigaContatori = new HBox(30);
        rigaContatori.setAlignment(Pos.CENTER);
        lblAzioniContatore = new Label();
        lblAzioniContatore.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 14px;");
        lblCureContatore = new Label();
        lblCureContatore.setStyle("-fx-text-fill: #00FF7F; -fx-font-weight: bold; -fx-font-size: 14px;");
        rigaContatori.getChildren().addAll(lblAzioniContatore, lblCureContatore);

        HBox rigaPulsanti = new HBox(12);
        rigaPulsanti.setAlignment(Pos.CENTER);
        btnAttacco1 = creaBottoneAzione("⚔ Attacca 1", "#b71c1c", atk1);
        btnAttacco2 = creaBottoneAzione("⚔ Attacca 2", "#880e4f", atk2);
        Button btnAttaccoArea = creaBottoneAzione("💥 Attacco Area", "#e65100", atkArea);
        Button btnCura = creaBottoneAzione("🧪 Cura Rapida", "#2e7d32", cura);

        rigaPulsanti.getChildren().addAll(btnAttacco1, btnAttacco2, btnAttaccoArea, btnCura);
        contenitore.getChildren().addAll(rigaContatori, rigaPulsanti);

        return contenitore;
    }

    private Button creaBottoneAzione(String testo, String colore, Runnable azione) {
        Button btn = new Button(testo);
        btn.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-cursor: hand;");
        btn.setPadding(new Insets(8, 12, 8, 12));
        btn.setOnAction(e -> azione.run());
        return btn;
    }

    public Pane creaPannelloSelezioneZonaConMappa(List<ZoneConfig> zone, Consumer<Integer> selezionaZonaCallback) {
        menuZonaBox = new Pane();
        menuZonaBox.setPrefSize(760, 420);

        Texture mappa = texture("mappa.png");
        mappa.setFitWidth(760);
        mappa.setFitHeight(420);

        Pane bordo = new Pane();
        bordo.setPrefSize(760, 420);
        bordo.setStyle("-fx-border-color: #8B4513; -fx-border-width: 4px; -fx-border-radius: 6px;");
        bordo.setMouseTransparent(true);

        menuZonaBox.getChildren().addAll(mappa, bordo);

        for (ZoneConfig z : zone) {
            Button btn = new Button(z.nome());
            btn.setLayoutX(z.buttonX());
            btn.setLayoutY(z.buttonY());
            btn.setOnAction(e -> selezionaZonaCallback.accept(z.id()));
            bottoniMappa.put(z.id(), btn);
            menuZonaBox.getChildren().add(btn);
        }

        menuZonaBox.setVisible(false);
        return menuZonaBox;
    }

    public VBox creaPannelloSelezioneDifficolta(Consumer<Difficilita> avviaOndataCallback) {
        menuDifficoltaBox = new VBox(15);
        menuDifficoltaBox.setPadding(new Insets(20));
        menuDifficoltaBox.setAlignment(Pos.CENTER);
        menuDifficoltaBox.setStyle("-fx-background-color: rgba(10, 10, 20, 0.98); -fx-border-color: #FFD700; -fx-border-width: 3px; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        menuDifficoltaBox.setPrefSize(450, 280);

        Label titolo = new Label("SCEGLI IL MOLTIPLICATORE DIFFICOLTÀ");
        titolo.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");

        Label descr = new Label("Sconfiggi la modalità 'Difficile' per sbloccare la zona successiva!");
        descr.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        Button btnBase = creaBottoneDifficolta("🟢 Base (1.0x)\nStatistiche Standard", "#2e7d32", Difficilita.BASE, avviaOndataCallback);
        Button btnMedia = creaBottoneDifficolta("🟡 Media (1.5x)\n+50% Danni e HP Nemici", "#f57f17", Difficilita.MEDIA, avviaOndataCallback);
        Button btnDifficile = creaBottoneDifficolta("🔴 Difficile (2.0x)\n2x Danni e HP (Richiesto Sblocco)", "#c62828", Difficilita.DIFFICILE, avviaOndataCallback);

        menuDifficoltaBox.getChildren().addAll(titolo, descr, btnBase, btnMedia, btnDifficile);
        menuDifficoltaBox.setVisible(false);

        return menuDifficoltaBox;
    }

    private Button creaBottoneDifficolta(String testo, String colore, Difficilita diff, Consumer<Difficilita> callback) {
        Button btn = new Button(testo);
        btn.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setPrefWidth(350);
        btn.setOnAction(e -> callback.accept(diff));
        return btn;
    }

    public VBox creaPannelloStatistiche(Consumer<Statistica> allocaStatCallback, Runnable optAttaccoSingolo, Runnable optAttaccoArea) {
        menuStatisticheBox = new VBox(10);
        menuStatisticheBox.setPadding(new Insets(15));
        menuStatisticheBox.setAlignment(Pos.CENTER);
        menuStatisticheBox.setStyle("-fx-background-color: rgba(18, 18, 24, 0.95); -fx-border-color: #FFD700; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        menuStatisticheBox.setPrefSize(440, 360);

        Label titolo = new Label("SCHEDA & POTENZIAMENTO EROE");
        titolo.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 15px;");

        lblPuntiDisponibili = new Label();
        lblPuntiDisponibili.setStyle("-fx-text-fill: #00FF7F; -fx-font-size: 13px; -fx-font-weight: bold;");

        lblStatForza = new Label(); lblStatForza.setStyle("-fx-text-fill: white; -fx-font-size: 12px;"); lblStatForza.setPrefWidth(280);
        lblStatHp = new Label(); lblStatHp.setStyle("-fx-text-fill: white; -fx-font-size: 12px;"); lblStatHp.setPrefWidth(280);
        lblStatDifesa = new Label(); lblStatDifesa.setStyle("-fx-text-fill: white; -fx-font-size: 12px;"); lblStatDifesa.setPrefWidth(280);

        HBox rigaForza = creaRigaStatistica(lblStatForza, () -> allocaStatCallback.accept(Statistica.FORZA));
        HBox rigaHp = creaRigaStatistica(lblStatHp, () -> allocaStatCallback.accept(Statistica.HP));
        HBox rigaDifesa = creaRigaStatistica(lblStatDifesa, () -> allocaStatCallback.accept(Statistica.DIFESA));

        Label lblTitoloAbilita = new Label("🔥 POTENZIAMENTO ABILITÀ");
        lblTitoloAbilita.setStyle("-fx-text-fill: #FF8C00; -fx-font-weight: bold; -fx-font-size: 14px;");

        lblAbilitaSingola = new Label();
        lblAbilitaSingola.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        HBox rigaSkillSingola = creaRigaStatistica(lblAbilitaSingola, optAttaccoSingolo);

        lblAbilitaArea = new Label();
        lblAbilitaArea.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        HBox rigaSkillArea = creaRigaStatistica(lblAbilitaArea, optAttaccoArea);

        Button btnChiudi = new Button("Chiudi");
        btnChiudi.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand;");
        btnChiudi.setOnAction(e -> menuStatisticheBox.setVisible(false));

        menuStatisticheBox.getChildren().addAll(
                titolo, lblPuntiDisponibili, rigaForza, rigaHp, rigaDifesa,
                lblTitoloAbilita, rigaSkillSingola, rigaSkillArea, btnChiudi
        );
        menuStatisticheBox.setVisible(false);

        return menuStatisticheBox;
    }

    private HBox creaRigaStatistica(Label labelStat, Runnable azione) {
        HBox riga = new HBox(10);
        riga.setAlignment(Pos.CENTER_LEFT);
        Button btnPiu = new Button("+");
        btnPiu.setStyle("-fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
        btnPiu.setOnAction(e -> azione.run());
        riga.getChildren().addAll(labelStat, btnPiu);
        return riga;
    }

    public void toggleMenuStatistiche() { menuStatisticheBox.setVisible(!menuStatisticheBox.isVisible()); }
    public void mostraMenuZone(boolean visibile) { menuZonaBox.setVisible(visibile); }
    public void mostraMenuDifficolta(boolean visibile) { menuDifficoltaBox.setVisible(visibile); }
    public boolean isQualsiasiMenuVisibile() {
        return (menuZonaBox != null && menuZonaBox.isVisible()) || (menuDifficoltaBox != null && menuDifficoltaBox.isVisible());
    }

    public void aggiornaStatoBottoniZone(List<ZoneConfig> zone, int maxSbloccato) {
        for (ZoneConfig z : zone) {
            Button btn = bottoniMappa.get(z.id());
            if (btn == null) continue;

            if (z.id() <= maxSbloccato) {
                btn.setText(z.nome() + " (Nv. " + z.livelloBase() + "+)");
                btn.setDisable(false);
                btn.setStyle("-fx-background-color: rgba(20, 30, 20, 0.85); -fx-text-fill: #00FF7F; -fx-font-weight: bold; -fx-font-size: 11px; -fx-border-color: #00FF7F; -fx-border-width: 2px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-cursor: hand;");
            } else {
                btn.setText("🔒 BLOCCATA");
                btn.setDisable(true);
                btn.setStyle("-fx-background-color: rgba(30, 30, 30, 0.9); -fx-text-fill: #ff4444; -fx-font-weight: bold; -fx-font-size: 11px; -fx-border-color: #ff4444; -fx-border-width: 2px; -fx-border-radius: 6px; -fx-background-radius: 6px;");
            }
        }
    }

    public void aggiornaGraficaUI(BattleManager battle, ZoneManager zone) {
        if (uiEroeStats == null) return;

        Eroe eroe = battle.getEroe();
        List<Nemico> nemici = battle.getNemiciCorrenti();

        uiEroeStats.setText(
                eroe.getNome() + " | Zona: " + zone.getZonaCorrente().nome() + " (" + battle.getDifficoltaCorrente().getEtichetta() + ") - Ondata " + zone.getOndataCorrente() + "\n" +
                        "Forza: " + eroe.getForza() + " | Difesa: " + eroe.getDifesa()
        );

        if (barVitaEroe != null && barScudoEroe != null) {
            double progressoHp = (double) eroe.getHpAttuali() / eroe.getHpMax();
            double progressoScudo = (double) eroe.getScudoAttuale() / eroe.getScudoMax();

            barVitaEroe.setProgress(progressoHp);
            barScudoEroe.setProgress(progressoScudo);

            lblTestoScudoVisivo.setText("🛡 " + eroe.getScudoAttuale() + " / " + eroe.getScudoMax());
            lblTestoHpVisivo.setText("❤️ " + eroe.getHpAttuali() + " / " + eroe.getHpMax());

            barVitaEroe.setStyle(progressoHp < 0.3 ? "-fx-accent: #FF0000;" : "-fx-accent: #32CD32;");
        }

        if (barVitaEnemy1 != null && barVitaEnemy2 != null) {
            if (nemici.size() > 0 && nemici.get(0).getHpMax() > 0) {
                Nemico e1 = nemici.get(0);
                barVitaEnemy1.setVisible(true);
                lblTestoHpEnemy1.setVisible(true);
                btnAttacco1.setVisible(true);
                barVitaEnemy1.setProgress((double) e1.getHpAttuali() / e1.getHpMax());
                lblTestoHpEnemy1.setText(e1.getNome() + " (Nv." + e1.getLivello() + "): ❤️ " + e1.getHpAttuali() + " / " + e1.getHpMax());
                btnAttacco1.setText("⚔ " + e1.getNome());
            } else {
                barVitaEnemy1.setVisible(false);
                lblTestoHpEnemy1.setVisible(false);
                btnAttacco1.setVisible(false);
            }

            if (nemici.size() > 1 && nemici.get(1).getHpMax() > 0) {
                Nemico e2 = nemici.get(1);
                barVitaEnemy2.setVisible(true);
                lblTestoHpEnemy2.setVisible(true);
                btnAttacco2.setVisible(true);
                barVitaEnemy2.setProgress((double) e2.getHpAttuali() / e2.getHpMax());
                lblTestoHpEnemy2.setText(e2.getNome() + " (Nv." + e2.getLivello() + "): ❤️ " + e2.getHpAttuali() + " / " + e2.getHpMax());
                btnAttacco2.setText("⚔ " + e2.getNome());
            } else {
                barVitaEnemy2.setVisible(false);
                lblTestoHpEnemy2.setVisible(false);
                btnAttacco2.setVisible(false);
            }
        }

        uiAzioniText.setText(battle.isPlayerTurn() ? "TURNO GIOCATORE" : "TURNO NEMICO...");

        if (lblAzioniContatore != null) {
            lblAzioniContatore.setText("⚡ Azioni Rimaste: " + battle.getAzioniRimaste() + " / " + battle.getMAX_AZIONI());
            lblCureContatore.setText("🧪 Cure Rimaste: " + battle.getCureRimanenti() + " / " + battle.getMAX_CURE());
        }

        if (lblPuntiDisponibili != null) {
            lblPuntiDisponibili.setText("Punti Stat: " + eroe.getPuntiStatBonus() + " | Punti Abilità: " + eroe.getPuntiAbilita());
            lblStatForza.setText("⚔ Forza: " + eroe.getForza());
            lblStatHp.setText("❤️ HP Max: " + eroe.getHpMax());
            lblStatDifesa.setText("🛡 Difesa: " + eroe.getDifesa() + " (Scudo Max: " + eroe.getScudoMax() + ")");
        }

        if (lblAbilitaSingola != null && lblAbilitaArea != null) {
            lblAbilitaSingola.setText("⚔ Atk Singolo (Nv. " + eroe.getLvlAttaccoSingolo() + "/3)");
            lblAbilitaArea.setText("💥 Atk Area (Nv. " + eroe.getLvlAttaccoArea() + "/3)");
        }
    }

    public void mostraTestoDanno(int valore, Point2D pos, Color colore) {
        Text t = new Text((colore == Color.GREEN ? "+" : "-") + valore);
        t.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        t.setFill(colore);

        Entity textEntity = entityBuilder()
                .at(pos.getX() + 40, pos.getY() - 10)
                .view(t)
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(0.8))
                .interpolator(Interpolator.EASE_OUT)
                .translate(textEntity)
                .from(new Point2D(pos.getX() + 40, pos.getY() - 10))
                .to(new Point2D(pos.getX() + 40, pos.getY() - 60))
                .buildAndPlay();

        getEngineTimer().runOnceAfter(textEntity::removeFromWorld, Duration.seconds(0.8));
    }

    public void animaAttacco(Entity chiAttacca, Entity bersaglio) {
        if (chiAttacca == null || bersaglio == null) return;
        Point2D orig = chiAttacca.getPosition();
        Point2D dest = bersaglio.getPosition();

        animationBuilder()
                .duration(Duration.seconds(0.15))
                .translate(chiAttacca)
                .from(orig)
                .to(dest)
                .buildAndPlay();

        getEngineTimer().runOnceAfter(() -> {
            animationBuilder()
                    .duration(Duration.seconds(0.15))
                    .translate(chiAttacca)
                    .from(dest)
                    .to(orig)
                    .buildAndPlay();
        }, Duration.seconds(0.15));
    }

    public HBox creaMenuTopRight(Runnable azioneMappa, Runnable azioneStats) {
        Button btnMappa = new Button("🗺 Mappa (M)");
        btnMappa.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnMappa.setOnAction(e -> azioneMappa.run());

        Button btnStats = new Button("📊 Stats (C)");
        btnStats.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnStats.setOnAction(e -> azioneStats.run());

        HBox menuTopRight = new HBox(10, btnMappa, btnStats);
        menuTopRight.setAlignment(Pos.CENTER);
        return menuTopRight;
    }

    public VBox creaPannelloGameOver(Runnable tornaAlMenuCallback) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: rgba(30, 0, 0, 0.95); -fx-border-color: #FF0000; -fx-border-width: 3px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        box.setPrefSize(400, 200);

        Label lblGameOver = new Label("☠ SEI STATO SCONFITTO ☠");
        lblGameOver.setStyle("-fx-text-fill: #FF0000; -fx-font-weight: bold; -fx-font-size: 20px;");

        Button btnMenu = new Button("🏠 Torna al Menu Principale");
        btnMenu.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        btnMenu.setPadding(new Insets(10, 15, 10, 15));
        btnMenu.setOnAction(e -> tornaAlMenuCallback.run());

        box.getChildren().addAll(lblGameOver, btnMenu);
        return box;
    }
}