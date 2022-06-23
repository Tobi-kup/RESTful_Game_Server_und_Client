package com.example.restservice;
import org.json.simple.parser.ParseException;
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
        Random r = new Random();


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

            else if(eingabe.equals("visualize")){
                ui.createSingleplayerGame();
                ui.player1place(5);
                System.out.println(ui.visualize());
        }


            //Spiel starten:
            else if (eingabe.equals("create")) {
                System.out.println("Möchten Sie gegen einen anderen Spieler oder gegen den Computer spielen?");
                System.out.println("Geben Sie 'm' um gegen einen anderen Spieler zu spielen.");
                System.out.println("Geben Sie 's' um gegen den Computer zu spielen.");
                eingabe = s.nextLine();

                //Gegen anderen Spieler
                if(eingabe.equals("m")) {
                    System.out.println(ui.createMultiplayerGame());
                    while(!ui.checkIfStartable().equals("Das Spiel kann gestartet werden")){ int setzeDieseVariableEinfachSoAuf5 = 5;}

                    while(ui.checkWinner().equals("")) {

                        //Nachdem Spieler 2 seinen Zug gemacht hat, macht Spieler 1 seinen
                        if (ui.checkLastMove().equals("Spieler 1 ist am Zug")) {
                            System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");
                            int spalte = s.nextInt();

                            while (ui.checkLastMove().equals("Spieler 1 ist am Zug")) System.out.println(ui.player1place(spalte));
                                System.out.println(ui.visualize());
                        }

                        if (ui.checkWinner().equals("R")) System.out.println("Du hast gewonnen!");
                        else System.out.println("Du hast leider verloren");
                    }


                //Gegen den Computer
                } else if(eingabe.equals("s")){
                    System.out.println(ui.createSingleplayerGame());

                    while(ui.checkWinner().equals("")) {

                        //Der User macht  seinen Zug
                        if(ui.checkLastMove().equals("Spieler 1 ist am Zug")) {
                            System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");
                            int spalte = s.nextInt();
                            while(ui.checkLastMove().equals("Spieler 1 ist am Zug")) System.out.println(ui.player1place(spalte));
                            System.out.println(ui.visualize());

                        //Der Computer macht so lange zufällige Züge, bis einer gültig ist, denn er kann versuchen auf eine bereits gefüllte Zeile werfen zu wollen
                        } else while(ui.checkLastMove().equals("Spieler 2 ist am Zug")) {
                            System.out.println("Der Computer hat folgenden Zug gemacht:");
                            ui.player2place(r.nextInt(6) + 1);
                            System.out.println(ui.visualize());
                        }
                    }

                    if(ui.checkWinner().equals("R")) System.out.println("Du hast gewonnen!");
                    else System.out.println("Du hast leider verloren");

                } else System.out.println("Keine gueltige Eingabe");
            }


            //Spiel beitreten:
            else if (eingabe.equals("join")) {
                System.out.println("Geben Sie den Namen des Spielers ein, mit dem Sie spielen wollen, sofern er ein Spiel gestartet hat.");
                String enemy = s.nextLine();
                eingabe = ui.join(enemy);
                if(eingabe.equals("Sie sind dem Spiel erfolreich beigetreten.")){

                    //Nachdem Spieler 1 seinen Zug gemacht hat, macht Spieler 2 seinen
                    if (ui.checkLastMove().equals("Spieler 2 ist am Zug")) {
                        System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");
                        int spalte = s.nextInt();

                        while (ui.checkLastMove().equals("Spieler 1 ist am Zug")) System.out.println(ui.player2place(spalte));
                        System.out.println(ui.visualize());
                    }

                    if (ui.checkWinner().equals("Y")) System.out.println("Du hast gewonnen!");
                    else System.out.println("Du hast leider verloren");


                } else System.out.println(eingabe);

            }


            //Löschen
            else if (eingabe.equals("delete")) {
                System.out.println(ui.delete()+ "\n");
                angemeldet = false;
            }


            //Ausloggen
            else if (eingabe.equals("logout")) {
                angemeldet = false;
            }


            else{
                System.out.println("Keine gueltige Eingabe");
            }
        }

    }
}

