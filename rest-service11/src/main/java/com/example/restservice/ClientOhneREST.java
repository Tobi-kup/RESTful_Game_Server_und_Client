package com.example.restservice;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.*;

public class ClientOhneREST {

    public static void main(String[] args) throws IOException, ParseException {

        String name = "";
        String password = "";
        Scanner s = new Scanner(System.in);
        UserInteraction ui = new UserInteraction();
        boolean angemeldet = false;
        boolean registered = false;
        boolean exit = false;
        String ausgabe = "";
        Random r = new Random();

        String spieler1 = "";
        String spieler2 = "";
        String gameFormat = "";
        String enemy = "";

        String eingabe = "";

        while (exit == false) {
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


                //Wenn sich ein Spieler anmeldet, der noch mitten in einem Spiel war
                if (spieler1.equals(name) || spieler2.equals(name)) {
                    System.out.println("Das Spiel wird fortgesetzt.");

                    //Fortsetzung vom Singleplayer game
                    if (spieler1.equals(name) && gameFormat.equals("singeplayer")) {
                        System.out.println(ui.visualize(name));
                        while (ui.checkWinner(name).equals(" ")) {

                            //Der User macht seinen Zug
                            if (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                while (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                    System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");

                                    //Mitten im Spiel ausloggen
                                    eingabe = s.nextLine();
                                    if (eingabe.equals("logout")) {
                                        angemeldet = false;
                                        break;

                                    } else {
                                        int spalte = Integer.parseInt(eingabe);
                                        System.out.println(ui.player1place(spalte, name));
                                    }
                                }
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                                System.out.println(ui.visualize(name));

                                //Der Computer macht so lange zufällige Züge, bis einer gültig ist, denn er kann versuchen auf eine bereits gefüllte Zeile werfen zu wollen
                            } else {
                                System.out.println("Der Computer hat folgenden Zug gemacht:");
                                while (ui.checkLastMove(name).equals("Spieler 2 ist am Zug")) {
                                    ui.player2place(r.nextInt(6) + 1, name);
                                }
                                System.out.println(ui.visualize(name));
                            }
                        }

                        if (ui.checkWinner(name).equals("R")){
                            System.out.println("Du hast gewonnen!");
                            spieler1 = "";
                        }

                        else if (ui.checkWinner(name).equals("Y")){
                            System.out.println("Du hast leider verloren");
                            spieler1 = "";
                        }
                    }


                    //Fortsetzung vom Multiplayer game aus Sicht von Spieler 1
                    else if (spieler1.equals(name) && gameFormat.equals("multiplayer")) {

                        while (ui.checkWinner(name).equals(" ")) {
                            //Nachdem Spieler 2 seinen Zug gemacht hat, macht Spieler 1 seinen
                            if (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                System.out.println(ui.visualize(name));
                                while (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                    System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");

                                    //Mitten im Spiel ausloggen
                                    eingabe = s.nextLine();
                                    if (eingabe.equals("logout")) {
                                        angemeldet = false;
                                        break;

                                    } else {
                                        int spalte = Integer.parseInt(eingabe);
                                        System.out.println(ui.player1place(spalte, name));
                                    }
                                }
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                                System.out.println(ui.visualize(name));
                            }
                            else {
                                eingabe = s.nextLine();
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                            }
                        }
                        if (ui.checkWinner(name).equals("R")){
                            System.out.println("Du hast gewonnen!");
                            spieler1 = "";
                        }

                        else if (ui.checkWinner(name).equals("Y")){
                            System.out.println("Du hast leider verloren");
                            spieler1 = "";
                        }
                    }


                    //Fortsetzung vom Multiplayer game aus Sicht von Spieler 2
                    else if (spieler2.equals(name) && gameFormat.equals("multiplayer")) {
                        while (ui.checkWinner(enemy).equals(" ")) {

                            //Nachdem Spieler 1 seinen Zug gemacht hat, macht Spieler 2 seinen
                            if (ui.checkLastMove(enemy).equals("Spieler 2 ist am Zug")) {
                                System.out.println(ui.visualize(enemy));
                                while (ui.checkLastMove(enemy).equals("Spieler 2 ist am Zug")) {
                                    System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");

                                    //Mitten im Spiel ausloggen
                                    eingabe = s.nextLine();
                                    if (eingabe.equals("logout")) {
                                        angemeldet = false;
                                        break;

                                    } else {
                                        int spalte = Integer.parseInt(eingabe);
                                        System.out.println(ui.player2place(spalte, enemy));
                                    }
                                }
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                                System.out.println(ui.visualize(enemy));
                            }
                            else {
                                eingabe = s.nextLine();
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                            }
                        }
                        if (ui.checkWinner(enemy).equals("Y")){
                            System.out.println("Du hast gewonnen!");
                            spieler2 = "";
                        }

                        else if (ui.checkWinner(enemy).equals("R")){
                            System.out.println("Du hast leider verloren");
                            spieler2 = "";
                        }
                    }
                }

                else {

                    eingabe = s.nextLine();


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
                    else if (eingabe.equals("visualize")) {
                        ui.createSingleplayerGame(name);
                        ui.player1place(5, name);
                        System.out.println(ui.visualize(name));
                    }


                    //Spiel starten:
                    else if (eingabe.equals("create")) {
                        spieler1 = name;
                        System.out.println("Möchten Sie gegen einen anderen Spieler oder gegen den Computer spielen?");
                        System.out.println("Geben Sie 'm' um gegen einen anderen Spieler zu spielen.");
                        System.out.println("Geben Sie 's' um gegen den Computer zu spielen.");
                        eingabe = s.nextLine();

                        //Gegen anderen Spieler
                        if (eingabe.equals("m")) {
                            gameFormat = "multiplayer";
                            System.out.println(ui.createMultiplayerGame(name));
                            while (!ui.checkIfStartable(name).equals("Das Spiel kann gestartet werden")) {
                                eingabe = s.nextLine();
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                                int setzeDieseVariableEinfachSoAuf5 = 5;
                            }

                            if (eingabe.equals("logout")) {
                                angemeldet = false;
                                break;
                            }

                            while (ui.checkWinner(name).equals(" ")) {
                                //Nachdem Spieler 2 seinen Zug gemacht hat, macht Spieler 1 seinen
                                if (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                    System.out.println(ui.visualize(name));
                                    while (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                        System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");

                                        //Mitten im Spiel ausloggen
                                        eingabe = s.nextLine();
                                        if (eingabe.equals("logout")) {
                                            angemeldet = false;
                                            break;

                                        } else {
                                            int spalte = Integer.parseInt(eingabe);
                                            System.out.println(ui.player1place(spalte, name));
                                        }
                                    }
                                    if (eingabe.equals("logout")) {
                                        angemeldet = false;
                                        break;
                                    }
                                    System.out.println(ui.visualize(name));
                                }
                            }
                            if (ui.checkWinner(name).equals("R")){
                                System.out.println("Du hast gewonnen!");
                                spieler1 = "";
                            }

                            else if (ui.checkWinner(name).equals("Y")){
                                System.out.println("Du hast leider verloren");
                                spieler1 = "";
                            }
                        }


                            //Gegen den Computer
                        else if (eingabe.equals("s")) {
                            gameFormat = "singeplayer";
                            System.out.println(ui.createSingleplayerGame(name));

                            while (ui.checkWinner(name).equals(" ")) {

                                //Der User macht seinen Zug
                                if (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                    while (ui.checkLastMove(name).equals("Spieler 1 ist am Zug")) {
                                        System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");

                                        //Mitten im Spiel ausloggen
                                        eingabe = s.nextLine();
                                        if (eingabe.equals("logout")) {
                                            angemeldet = false;
                                            break;

                                        } else {
                                            int spalte = Integer.parseInt(eingabe);
                                            System.out.println(ui.player1place(spalte, name));
                                        }
                                    }
                                    if (eingabe.equals("logout")) {
                                        angemeldet = false;
                                        break;
                                    }
                                    System.out.println(ui.visualize(name));

                                    //Der Computer macht so lange zufällige Züge, bis einer gültig ist, denn er kann versuchen auf eine bereits gefüllte Zeile werfen zu wollen
                                } else {
                                    System.out.println("Der Computer hat folgenden Zug gemacht:");
                                    while (ui.checkLastMove(name).equals("Spieler 2 ist am Zug")) {
                                        ui.player2place(r.nextInt(6) + 1, name);
                                    }
                                    System.out.println(ui.visualize(name));
                                }
                            }

                            if (ui.checkWinner(name).equals("R")){
                                System.out.println("Du hast gewonnen!");
                                spieler1 = "";
                            }

                            else if (ui.checkWinner(name).equals("Y")){
                                System.out.println("Du hast leider verloren");
                                spieler1 = "";
                            }

                        } else System.out.println("Keine gueltige Eingabe");
                    }


                    //Spiel beitreten:
                    else if (eingabe.equals("join")) {
                        gameFormat = "multiplayer";
                        System.out.println("Geben Sie den Namen des Spielers ein, mit dem Sie spielen wollen, sofern er ein Spiel gestartet hat.");
                        enemy = s.nextLine();
                        ausgabe = ui.join(enemy);
                        if (ausgabe.equals("Sie sind dem Spiel erfolreich beigetreten.")) {
                            spieler2 = name;
                            System.out.println(ausgabe);

                            while(!ui.checkLastMove(enemy).equals("Spieler 2 ist am Zug")) {
                                eingabe = s.nextLine();
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;

                                } else System.out.println("Ungueltige Eingabe");
                            }

                            while (ui.checkWinner(enemy).equals(" ")) {

                                //Nachdem Spieler 1 seinen Zug gemacht hat, macht Spieler 2 seinen
                                if (ui.checkLastMove(enemy).equals("Spieler 2 ist am Zug")) {
                                    System.out.println(ui.visualize(enemy));
                                    while (ui.checkLastMove(enemy).equals("Spieler 2 ist am Zug")) {
                                        System.out.println("\nWirf die Scheibe in die Spalte: (1-7)");

                                        //Mitten im Spiel ausloggen
                                        eingabe = s.nextLine();
                                        if (eingabe.equals("logout")) {
                                            angemeldet = false;
                                            break;

                                        } else {
                                            int spalte = Integer.parseInt(eingabe);
                                            System.out.println(ui.player2place(spalte, enemy));
                                        }
                                    }
                                    if (eingabe.equals("logout")) {
                                        angemeldet = false;
                                        break;
                                    }
                                    System.out.println(ui.visualize(enemy));
                                }
                                if (eingabe.equals("logout")) {
                                    angemeldet = false;
                                    break;
                                }
                            }
                            if (ui.checkWinner(enemy).equals("Y")){
                                System.out.println("Du hast gewonnen!");
                                spieler2 = "";
                            }

                            else if (ui.checkWinner(enemy).equals("R")){
                                System.out.println("Du hast leider verloren");
                                spieler2 = "";
                            }

                        } else System.out.println(ausgabe);
                    }


                    //Löschen
                    else if (eingabe.equals("delete")) {
                        System.out.println(ui.delete() + "\n");
                        angemeldet = false;
                    }


                    //Ausloggen
                    else if (eingabe.equals("logout")) {
                        angemeldet = false;
                    }


                    //Client beenden
                    else if (eingabe.equals("exit")) {
                        angemeldet = false;
                        exit = true;
                    } else {
                        System.out.println("Keine gueltige Eingabe");
                    }
                }
            }
        }
    }
}