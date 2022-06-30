package com.example.restservice;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.*;

/**
 * Klasse, die den Client, des Projektes darstellt.
 * Sie ruft die im Server generierten URIs auf und arbeitet mit der Ausgabe dieser.
 */

public class Client {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        /**
         * 2 Attribute vom Typ String, zur späteren Speicherung vom Namen und Password des Nutzkontos
         * Instanz der Klasser Scanner für die Terminaleingaben
         * 2 Attribute vom Typ boolean: angemeldet und registered für den Programmdurchlauf
         * Instanz der Klasse Random um die Züge des Computers zu simulieren
         * 1 Attribut vom Typ String, zur späteren Überschreibung durch die Ausgaben des Servers nach Aufruf der URIs
         * Instanz der Klasse RestTemplate, um die Daten Ausgabe des Servers zu erhalten
         */
        String name;
        String password;
        Scanner s = new Scanner(System.in);
        boolean angemeldet = false;
        boolean registered = false;
        String ausgabe = "";
        Random r = new Random();
        RestTemplate rt = new RestTemplate();

        String eingabe = "";

        while (angemeldet == false || registered == false) {
            System.out.println("Moechten Sie sich registrieren oder einloggen? \n" +
                    "Waehlen Sie 'r' um sich zu registrieren oder 'l' um sich einzuloggen.");

            eingabe = s.nextLine();

            /**
             * code für das Registrieren
             * checkt ob "r" eingegeben wird mit dem Scanner
             * Fallunterscheidung durch die Ausgabe des Servers
             * wenn registriert wird registered auf true gesetzt
             */
            if (eingabe.equals("r")) {
                System.out.println("Geben sie bitte ihren gewuenschten Nutzernamen und ihr gewuenschtes Passwort in dem Format \nname \npassword\nein.");
                name = s.nextLine();
                password = s.nextLine();
                ausgabe = rt.getForObject("http://localhost:8080/signup?name="+name+"&password="+password+"", String.class);

                if (ausgabe.equals("Erfolgreich registiert.")) {
                    System.out.println(ausgabe + "\n");
                    registered = true;

                } else if (ausgabe == "User existiert bereits.") {
                    System.out.println(ausgabe + "\n");
                    registered = true;

                } else if (ausgabe.equals("Die maximale Anzahl an registrierten Nutzern ist bereits erreicht.")) {
                    System.out.println(ausgabe + "\n");
                    System.out.println("Melden Sie sich bitte mit einem Konto an, wenn Sie eins besitzen, wenn nicht, tut es uns leid.");
                }


                /**
                 * code für das Einloggen
                 * checkt ob "l" eingegeben wird mit dem Scanner
                 * Fallunterscheidung durch die Ausgabe des Servers
                 * wenn eingeloggt, wird registered und angemeldet auf true gesetzt und somit die Schleife beendet
                 */
            } else if (eingabe.equals("l")) {
                System.out.println("Geben sie bitte ihren Nutzernamen und ihr Passwort in dem Format \nname\npassword\nein.");
                name = s.nextLine();
                password = s.nextLine();
                ausgabe = rt.getForObject("http://localhost:8080/login?name="+name+"&password="+password+"", String.class);

                if (ausgabe.equals("Login erfolgreich.")) {
                    System.out.println(ausgabe + "\n");
                    angemeldet = true;
                    registered = true;

                } else if (ausgabe.equals("Es ist noch kein Nutzer registriert.")) {
                    System.out.println(ausgabe + "\n");
                    System.out.println("Wir bitten Sie sich zu registrieren.");

                } else if (ausgabe.equals("Passwort ist falsch.")) {
                    System.out.println(ausgabe + "\n");

                } else if (ausgabe.equals("Dieser Nutzer existiert nicht.")) {
                    System.out.println(ausgabe + "\n");
                }

            } else  System.out.println("Keine gueltige Eingabe\n");
        }

        System.out.println("\nGeben sie 'help' ein, um alle akzeptierten Befehle anzuzeigen.\n");

        while (angemeldet == true) {

            eingabe = s.nextLine();


            /**
             * Ausgabe um dem Benutzer alle möglichen Befehle zu zeigen
             */
            //Verfügbare Befehle ausgeben
            if (eingabe.equals("help")) {
                System.out.println("");
                System.out.println("Moechten Sie ein Spiel starten, einem Spiel beitreten oder sich ausloggen?");
                System.out.println("Geben Sie 'create' ein, um ein Spiel zu erstellen.");
                System.out.println("Geben Sie 'join' ein, um einem Spiel beizutreten.");
                System.out.println("Geben Sie 'logout' ein, um sich auszuloggen.");
                System.out.println("Geben Sie 'delete' ein, um ihr Konto zu loeschen.\n");
            }


            //temporär für die manuelle Ausgabe des Spielbretts gewesen
            else if(eingabe.equals("visualize")){
                rt.getForObject("http://localhost:8080/createSingleplayerGame", String.class);
                rt.getForObject("http://localhost:8080/player1place?column=1", String.class);
                System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));
            }


            /**
             * Ruft je nachdem die URI für das Erstellen eines Singeplayer Games oder eines Multiplayer Games auf
             * Prüft im Falle eines Multiplayer Games durch Aufruf einer URIs, ob das Spiel gestartet werden kann, also ob ein 2. Spieler beigetreten ist
             * Durch Aufruf weiterer URIs wird der Spielzustand überwacht, d.h. wer gerade am Zug ist und ob das Spiel schon beendet ist
             * Zudem ermöglichen die Aufrufe 2 entsprechender URIs den Spielern ihre Scheiben zu werfen
             */
            //Spiel starten:
            else if (eingabe.equals("create")) {
                System.out.println("Möchten Sie gegen einen anderen Spieler oder gegen den Computer spielen?");
                System.out.println("Geben Sie 'm' um gegen einen anderen Spieler zu spielen.");
                System.out.println("Geben Sie 's' um gegen den Computer zu spielen.");
                eingabe = s.nextLine();

                //Gegen anderen Spieler
                if(eingabe.equals("m")) {
                    System.out.println(rt.getForObject("http://localhost:8080/createMultiplayerGame", String.class));
                    while(!rt.getForObject("http://localhost:8080/checkIfStartable", String.class).equals("Das Spiel kann gestartet werden")){ Thread.sleep(1000);;}

                    while(rt.getForObject("http://localhost:8080/checkWinner", String.class).equals(" ")) {
                        Thread.sleep(1000);
                        //Nachdem Spieler 2 seinen Zug gemacht hat, macht Spieler 1 seinen
                        if (rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 1 ist am Zug")) {
                            System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));
                            while (rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 1 ist am Zug")) {
                                System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");
                                int spalte = s.nextInt();
                                System.out.println(rt.getForObject("http://localhost:8080/player1place?column=" + spalte + "", String.class));
                            }
                            System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));
                        }
                    }
                        if (rt.getForObject("http://localhost:8080/checkWinner", String.class).equals("R")) System.out.println("Du hast gewonnen!");
                        else System.out.println("Du hast leider verloren");
                    }


                //Gegen den Computer
                else if(eingabe.equals("s")){
                    System.out.println(rt.getForObject("http://localhost:8080/createSingleplayerGame", String.class));

                    ausgabe = rt.getForObject("http://localhost:8080/checkWinner", String.class);
                    while(ausgabe.equals(" ")) {

                        //Der User macht seinen Zug
                        if(rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 1 ist am Zug")) {
                            while(rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 1 ist am Zug")) {
                                System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");
                                int spalte = s.nextInt();
                                System.out.println(rt.getForObject("http://localhost:8080/player1place?column=" + spalte + "", String.class));
                            }
                            System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));

                            //Der Computer macht so lange zufällige Züge, bis einer gültig ist, denn er kann versuchen auf eine bereits gefüllte Zeile werfen zu wollen
                        } else while(rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 2 ist am Zug")) {
                            System.out.println("Der Computer hat folgenden Zug gemacht:");
                            int zufaelligeSpalte = r.nextInt(6) + 1;
                            rt.getForObject("http://localhost:8080/player2place?column="+zufaelligeSpalte+"", String.class);
                            System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));
                        }
                        ausgabe = rt.getForObject("http://localhost:8080/checkWinner", String.class);
                    }

                    if(rt.getForObject("http://localhost:8080/checkWinner", String.class).equals("R")) System.out.println("Du hast gewonnen!");
                    else System.out.println("Du hast leider verloren");

                } else System.out.println("Keine gueltige Eingabe");
            }


            //Spiel beitreten:
            /**
             * Ruft die URI für das beitreten eines Spiels auf und praktiziert durch Aufufe weiterer URIs die Züge des 2. Spielers
             */
            else if (eingabe.equals("join")) {
                System.out.println("Geben Sie den Namen des Spielers ein, mit dem Sie spielen wollen, sofern er ein Spiel gestartet hat.");
                String enemy = s.nextLine();
                ausgabe = rt.getForObject("http://localhost:8080/join?name="+enemy+"", String.class);
                if(ausgabe.equals("Sie sind dem Spiel erfolreich beigetreten.")){

                    while (rt.getForObject("http://localhost:8080/checkWinner", String.class).equals(" ")) {

                        //Nachdem Spieler 1 seinen Zug gemacht hat, macht Spieler 2 seinen
                        if (rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 2 ist am Zug")) {
                            System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));
                            while (rt.getForObject("http://localhost:8080/checkLastMove", String.class).equals("Spieler 2 ist am Zug")) {
                                System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");
                                int spalte = s.nextInt();
                                System.out.println(rt.getForObject("http://localhost:8080/player2place?column=" + spalte + "", String.class));
                            }
                            System.out.println(rt.getForObject("http://localhost:8080/visualize", String.class));
                        }
                    }

                    if (rt.getForObject("http://localhost:8080/checkWinner", String.class).equals("Y")) System.out.println("Du hast gewonnen!");
                    else System.out.println("Du hast leider verloren");


                } else System.out.println(ausgabe);
            }


            //Löschen
            /**
             * Ruft die URI für das Löschen des aktuell angemeldeten Nutzerkontos auf und die URI für das Schließen des Servers
             * und beendet die aktuelle Schleife und somit den Client
             */
            else if (eingabe.equals("delete")) {
                System.out.println(rt.getForObject("http://localhost:8080/delete", String.class));
                angemeldet = false;
                System.out.println(rt.getForObject("http://localhost:8080/shutdown", String.class));
            }


            //Ausloggen
                /**
                 * beendet die aktuelle Schleife und somit den Client, wenn der Benutzer "logout" eingibt
                 */
            else if (eingabe.equals("logout")) {
                    angemeldet = false;
                }


            else{
                System.out.println("Keine gueltige Eingabe");
            }
        }

    }
}