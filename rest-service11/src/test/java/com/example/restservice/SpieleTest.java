package com.example.restservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;


public class SpieleTest {

    RestTemplate rt = new RestTemplate();


    @Test
    @DisplayName("Login ohne vorherige Registrierung")
    void loginOhneRegistrierung(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Test0&password=123", String.class);

        if(eingabe.equals("Es ist noch kein Nutzer registriert.") || eingabe.equals("Dieser Nutzer existiert nicht."))System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Regisrierung eines ersten Benutzers")
    void registriereErstenBenutzer(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test1&password=123", String.class);
        if(eingabe.equals("Erfolgreich registiert.")) System.out.println("True");
        else System.out.println("False");

    }


    @Test
    @DisplayName("Erfolgreiche Anmeldung")
    void loginErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test3&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test3&password=123", String.class);

        if(eingabe.equals("Login erfolgreich.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Login mit falschem Passwort")
    void loginMitFalschemPasswort(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test4&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test4&password=124", String.class);

        if(eingabe.equals("Passwort ist falsch.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Erstellung eines Singleplayer Games")
    void createSingleplayerGame(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test5&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test5&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createSingleplayerGame?name=Test5", String.class);
        if(eingabe.equals("Spiel erfolgreich gestartet. \nSie sind am Zug.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Erstellung eines Multiplayer Games")
    void createMultiplayerGame(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test6&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test6&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test6", String.class);
        if(eingabe.equals("Spiel erfolgreich gestartet, warte auf anderen Spieler...")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("2. Spieler tritt dem Multiplayer Game erfolgreich bei")
    void joinMultiplayerGameErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test7&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test7&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test7", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test7Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test7Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test7", String.class);

        if(eingabe.equals("Sie sind dem Spiel erfolreich beigetreten.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Versuch einem Spiel von einem nicht registrierten Spieler beizutreten")
    void joinMultiplayerGameHostExistiertNicht(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test8Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test8Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test8", String.class);

        if(eingabe.equals("Es ist kein Spieler mit diesem Namen registiert.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Versuch einem Spiel beizutreten, das bereits läuft")
    void joinMultiplayerGameDasBereitsLaeuft(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test9&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test9&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test9", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test9Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test9Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test9", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test9Spieler3&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test9Spieler3&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test9", String.class);

        if(eingabe.equals("Das Spiel läuft bereits.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Versuch einem nicht erstellten Spiel beizutreten")
    void joinMultiplayerGameNichtErstellt(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test10&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test10&password=123", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test10Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test10Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test10", String.class);

        if(eingabe.equals("Dieser Spieler hat kein Spiel erstellt.")) System.out.println("True");
        else System.out.println("False");
    }

    @Test
    @DisplayName("Ein Spieler wirft erfolgreich eine Scheibe ein")
    void place1placeErfolreich(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test11&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test11&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createSingleplayerGame?name=Test11", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=1&name=Test11", String.class);
        if(eingabe.equals("Erfolgreich abgelegt")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Ein Spieler wirft eine Scheibe ein in eine nicht existente Spalte")
    void place1placeSpalteGroesserAls7(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test12&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test12&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createSingleplayerGame?name=Test12", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=8&name=Test12", String.class);
        if(eingabe.equals("So viele Spalten gibt es nicht.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Ein Spieler wirft eine Scheibe ein in eine volle Spalte")
    void place1placeSpalteVoll(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test13&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test13&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createSingleplayerGame?name=Test13", String.class);
        for(int i=1; i<=7; i++) {
            eingabe = rt.getForObject("http://localhost:8080/player1place?column=1&name=Test13", String.class);
        }
        if(eingabe.equals("Diese Spalte ist bereits komplett gefüllt")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Ein Spieler wirft eine Scheibe während er nicht dran ist")
    void place1placeNichtDran(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test14&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test14&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test14", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test14Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test14Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test14", String.class);

        eingabe = rt.getForObject("http://localhost:8080/player1place?column=1&name=Test14", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=1&name=Test14", String.class);

        if(eingabe.equals("Sie sind aktuell nicht dran")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Ein Spieler gewinnt horinzontal")
    void winHorizontal(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test15&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test15&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test15", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test15Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test15Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test15", String.class);

        for(int i=1; i<=4; i++) {
            eingabe = rt.getForObject("http://localhost:8080/player1place?column="+i+"&name=Test15", String.class);
            eingabe = rt.getForObject("http://localhost:8080/player2place?column=7&name=Test15", String.class);
        }

        eingabe = rt.getForObject("http://localhost:8080/checkWinner?name=Test15", String.class);

        if(eingabe.equals("R")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Ein Spieler gewinnt vertikal")
    void winVertikal(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test16&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test16&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test16", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test16Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test16Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test16", String.class);

        for(int i=1; i<=4; i++) {
            eingabe = rt.getForObject("http://localhost:8080/player1place?column=1&name=Test16", String.class);
            eingabe = rt.getForObject("http://localhost:8080/player2place?column=7&name=Test16", String.class);
        }

        eingabe = rt.getForObject("http://localhost:8080/checkWinner?name=Test16", String.class);

        if(eingabe.equals("R")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Ein Spieler gewinnt diagonal")
    void winDiagonal(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test17&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test17&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/createMultiplayerGame?name=Test17", String.class);

        eingabe = rt.getForObject("http://localhost:8080/signup?name=Test17Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test17Spieler2&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/join?name=Test17", String.class);

        eingabe = rt.getForObject("http://localhost:8080/player1place?column=1&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player2place?column=2&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=2&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player2place?column=3&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=3&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player2place?column=4&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=3&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player2place?column=3&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=4&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player2place?column=4&name=Test17", String.class);
        eingabe = rt.getForObject("http://localhost:8080/player1place?column=4&name=Test17", String.class);

        eingabe = rt.getForObject("http://localhost:8080/checkWinner?name=Test17", String.class);

        if(eingabe.equals("R")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'delete' nach Anmeldung")
    void deleteErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test18&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/login?name=Test18&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/delete", String.class);

        if(eingabe.equals("Konto erfolgreich geloescht")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'delete' ohne Anmeldung")
    void deleteFehler(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Test18&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/delete", String.class);

        if(eingabe.equals("Sie müssen angemeldet sein, um ihr Konto zu löschen.")) System.out.println("True");
        else System.out.println("False");
    }

}