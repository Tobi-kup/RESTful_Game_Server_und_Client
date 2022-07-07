package com.example.restservice;
import org.json.simple.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Klasse zur Erstellung eines neuen Spiels.
 */
public class GameCreation {

    public GameCreation(){}

    /**
     * Erstellt eine .json Datei mit den Parametern Spieler1, Spieler2 und aktuellDran
     * für die Teilnehmer des Spiels und einzelne Parameter für die Spielfelder
     * mit Feld[Zeile][Spalte].
     *
     * @throws IOException bei Problemen mit FileWriter
     */

    public void newGame(String name) throws IOException{
        Path path = Paths.get("").toAbsolutePath();
        Path endpath = Path.of(path + "\\rest-service11\\json-Dateien");
        JSONObject newGame = new JSONObject();

        newGame.put("Spieler1", "");
        newGame.put("Spieler2", "");
        newGame.put("aktuellDran", "Spieler1");

        newGame.put("Feld11", " ");
        newGame.put("Feld12", " ");
        newGame.put("Feld13", " ");
        newGame.put("Feld14", " ");
        newGame.put("Feld15", " ");
        newGame.put("Feld16", " ");
        newGame.put("Feld17", " ");

        newGame.put("Feld21", " ");
        newGame.put("Feld22", " ");
        newGame.put("Feld23", " ");
        newGame.put("Feld24", " ");
        newGame.put("Feld25", " ");
        newGame.put("Feld26", " ");
        newGame.put("Feld27", " ");

        newGame.put("Feld31", " ");
        newGame.put("Feld32", " ");
        newGame.put("Feld33", " ");
        newGame.put("Feld34", " ");
        newGame.put("Feld35", " ");
        newGame.put("Feld36", " ");
        newGame.put("Feld37", " ");

        newGame.put("Feld41", " ");
        newGame.put("Feld42", " ");
        newGame.put("Feld43", " ");
        newGame.put("Feld44", " ");
        newGame.put("Feld45", " ");
        newGame.put("Feld46", " ");
        newGame.put("Feld47", " ");

        newGame.put("Feld51", " ");
        newGame.put("Feld52", " ");
        newGame.put("Feld53", " ");
        newGame.put("Feld54", " ");
        newGame.put("Feld55", " ");
        newGame.put("Feld56", " ");
        newGame.put("Feld57", " ");

        newGame.put("Feld61", " ");
        newGame.put("Feld62", " ");
        newGame.put("Feld63", " ");
        newGame.put("Feld64", " ");
        newGame.put("Feld65", " ");
        newGame.put("Feld66", " ");
        newGame.put("Feld67", " ");



        FileWriter file = new FileWriter(new File(String.valueOf(endpath),name + "-game.json"));
        file.write(newGame.toJSONString());
        file.close();
    }
}
