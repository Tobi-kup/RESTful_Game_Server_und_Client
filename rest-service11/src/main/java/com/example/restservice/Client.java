package com.example.restservice;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

public class Client {

    public static void main(String[] args) throws IOException, ParseException {

        String name;
        String password;
        Scanner s = new Scanner(System.in);
        UserInteraction ui = new UserInteraction();
        boolean angemeldet = false;
        boolean registered = false;
        String ausgabe = "";


        String eingabe = "";

        while (angemeldet == false || registered == false) {
            System.out.println("Moechten Sie sich registrieren oder einloggen? \n" +
                    "Waehlen Sie 'r' um sich zu registrieren oder 'l' um sich einzuloggen.");

            eingabe = s.nextLine();

            //Registrieren:
            if (eingabe.equals("r")) {
                System.out.println("Geben sie bitte ihren gewuenschten Nutzernamen und ihr gewuenschtes Passwort in dem Format \nname \npassword\nein.");
                name = s.nextLine();
                password = s.nextLine();
                ausgabe = ui.signup(name, password);
                if (ausgabe == "Erfolgreich registiert.") {
                    System.out.println(ausgabe);
                    registered = true;

                } else if (ausgabe == "User existiert bereits.") {
                    System.out.println(ausgabe);
                    registered = true;

                } else if (ausgabe == "Die maximale Anzahl an registrierten Nutzern ist bereits erreicht.") {
                    System.out.println(ausgabe);
                    System.out.println("Melden Sie sich bitte mit einem Konto an, wenn Sie eins besitzen, wenn nicht, tut es uns leid.");
                }

                //Einloggen:
            } else if (eingabe.equals("l")) {
                System.out.println("Geben sie bitte ihren Nutzernamen und ihr Passwort in dem Format \nname\npassword\nein.");
                name = s.nextLine();
                password = s.nextLine();
                ausgabe = ui.login(name, password);
                if (ausgabe == "Login erfolgreich.") {
                    System.out.println(ausgabe);
                    angemeldet = true;
                    registered = true;

                } else if (ausgabe == "Es ist noch kein Nutzer registriert.") {
                    System.out.println(ausgabe);
                    System.out.println("Wir bitten Sie sich zu registrieren.");

                } else if (ausgabe == "Passwort ist falsch.") {
                    System.out.println(ausgabe);

                } else if (ausgabe == "Dieser Nutzer existiert nicht.") {
                    System.out.println(ausgabe);
                }

            } else System.out.println("Ungueltige Eingabe\n");
        }

        System.out.println("\nGeben sie 'help' ein, um alle akzeptierten Befehle anzuzeigen.\n");

        while (angemeldet == true) {

            eingabe = s.nextLine();


            //Verfügbare Befehle ausgeben
            if (eingabe.equals("help")) {
                System.out.println("");
                System.out.println("Moechten Sie ein Spiel starten, einem Spiel beitreten oder sich ausloggen?");
                System.out.println("Geben Sie 'create' ein, um Gueter zu erwerben.");
                System.out.println("Geben Sie 'join' ein, um Gueter zu verkaufen.");
                System.out.println("Geben Sie 'logout' ein, um sich auszuloggen.");
                System.out.println("Geben Sie 'delete' ein, um ihr Konto zu loeschen.\n");
            }

            if(eingabe.equals("visualize")){
                ui.createSingleplayerGame();
                ui.player1place(5);
                System.out.println(ui.visualize());
        }


            //Spiel starten:
            if (eingabe.equals("create")) {
                System.out.println("Möchten Sie gegen einen anderen Spieler oder gegen den Computer spielen?");
                System.out.println("Geben Sie 'pvp' um gegen einen anderen Spieler zu spielen.");
                System.out.println("Geben Sie 'pve' um gegen den Computer zu spielen.");
                eingabe = s.nextLine();

                if(eingabe.equals("pvp")) {
                    System.out.println("Sie haben ein Spiel erfolgreich gestartet, es wird auf einen 2. Spieler gewartet...");

                } else if(eingabe.equals("pve")){

                }
            }


            //Spiel beitreten:
            if (eingabe.equals("join")) {
                System.out.println("Geben Sie den Namen des Spielers ein, mit dem Sie spielen wollen, sofern er ein Spiel gestartet hat.");
                String enemy = s.nextLine();

            }


            //Löschen
            if (eingabe.equals("delete")) {
                System.out.println(ui.delete()+ "\n");
                angemeldet = false;
            }


            //Ausloggen
            if (eingabe.equals("logout")) {
                angemeldet = false;
            }
        }

    }
}

