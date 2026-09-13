# RPG - Monster Assault

Progetto per il corso di Metodologie di Programmazione (AA 2025/26) - Università di Camerino.

Applicativo Java basato sul framework FXGL con interfaccia grafica RPG a turni: il giocatore controlla il proprio eroe, affronta ondate di nemici con difficoltà modulabile, gestisce abilità uniche, esplora una mappa interattiva divisa in zone ed evolve le proprie statistiche fino allo scontro finale con il Re Demone.

---

### Descrizione

Il gioco si sviluppa attraverso diverse meccaniche e componenti principali:

* **Gestione ed Evoluzione dell'Eroe:** Controllo dell'eroe con gestione dinamica di HP, scudo rigenerabile basato sulla difesa, punti stat allocabili (Forza, HP Max, Difesa) e un **albero delle abilità passive/attive** potenziabile (Attacco Singolo e Attacco Area con effetti di burn, critico e rubavita).
* **Mappa e Selezione Zone:** Esplorazione interattiva tramite mappa di gioco per accedere a 5 zone progressive (*Foresta Oscura, Entrata Caverna, Caverna Profonda, Vulcano, Castello Re Demone*).
* **Moltiplicatori di Difficoltà:** Selezione della difficoltà per ciascuna ondata (*Base 1.0x, Media 1.5x, Difficile 2.0x*). Il completamento della modalità Difficile è necessario per sbloccare i Punti Abilità extra e il passaggio alla zona successiva.
* **Sistema di Combattimento a Turni:** Battaglie strategiche con azioni limitate per turno, gestione delle fiaschette di cura e indicatore visivo avanzato con barra dello scudo e vita per eroe e nemici.
* **Boss Fight Dinamica:** Scontro finale nel Castello contro il Re Demone dotato di una meccanica speciale di trasformazione e potenziamento in Fase 2.

---

### Come eseguire il progetto

**Prerequisiti**
* JDK 21 o superiore
* Gradle

**Build**

```bash
./gradlew build
```

**Esecuzione**

```bash
./gradlew run
```

---

## 🤖 Uso di strumenti di AI
* Utilizzato Gemini per:

    * comprendere concetti teorici 
    * chiarire errori di compilazione
    * suggerimenti su struttura del codice
    * generare una prima versione di una funzione modificata e adattata manualmente testata e corretta personalmente

---

## Struttura del Progetto & Architettura SOLID
* Il codice è organizzato nel package it.unicam.cs.mpgc.rpg129040 applicando i principi SOLID:

     * **Model:** 
       * Contiene il dominio di gioco **(Eroe, Nemico, Personaggio, Statistica, Difficilita, ZoneConfig, EnemyConfig)**. Gestisce gli attributi, i livelli abilità e il calcolo dei danni.

  * **Repository:** 
    * Astrazione e gestione dei dati di configurazione tramite l'interfaccia IZoneRepository e la sua implementazione ZoneRepository (DIP e ISP).

  * **Service:**
    * Logica di business isolata:

       * **BattleManager:** Gestione di turni, azioni, cure, calcolo dei danni con difesa/scudo e condizioni di sblocco abilità (SRP).

       * **ZoneManager:** Gestione del progresso di gioco, avanzamento ondate e sblocco delle aree della mappa.

  * **Ui:** 
    * GameUIManager si occupa unicamente della costruzione e dell'aggiornamento dell'interfaccia grafica JavaFX/FXGL, separando il rendering dalla logica applicativa (SRP).

  * **HelloApplication:** Classe Main e Composition Root. Gestisce il loop di gioco, l'orchestra tra logica e UI tramite callback disaccoppiate (Runnable, Consumer) e il tracciamento dell'input utente.

---
## Comandi e Controlli
* **M:** Apre / Chiude il Menu della Mappa delle Zone

* **C:** Apre / Chiude la Scheda Eroe e il Menu Potenziamento Statistiche / Abilità

* **Mouse SX:** 
  * Interazione completa con i bottoni a schermo:

    * Selezione bersaglio attacco singolo (Attacca 1 / Attacca 2)

    * Attacco ad Area

    * Uso Fiaschetta di Cura

    * Selezione Zone sulla Mappa

    * Scelta Moltiplicatore Difficoltà

    * Allocazione Punti Statistica e Livelli Abilità
