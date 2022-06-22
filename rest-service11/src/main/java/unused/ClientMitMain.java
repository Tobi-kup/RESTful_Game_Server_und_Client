package unused;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class ClientMitMain {

    public static void main(String[] args) throws IOException, ParseException {

        String name;
        String password;
        Scanner s = new Scanner(System.in);
        UserInteractionMitJSON ui = new UserInteractionMitJSON();
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
                System.out.println("Moechten Sie etwas kaufen, etwas verkaufen, sich Informationen ueber ihr Konto ausgeben lassen, die Preise aktualisieren, Geld abzuheben, Geld einzahlen oder sich ausloggen?");
                System.out.println("Geben Sie 'buy' ein, um Gueter zu erwerben.");
                System.out.println("Geben Sie 'sell' ein, um Gueter zu verkaufen.");
                System.out.println("Geben Sie 'info' ein, um Informationen ueber ihr Konto zu erhalten.");
                System.out.println("Geben Sie 'refresh' ein, um die Preise zu aktualisieren.");
                System.out.println("Geben Sie 'prices' ein, um die aktuellen Preise anzuzeigen.");
                System.out.println("Geben Sie 'withdraw' um Geld abzuheben.");
                System.out.println("Geben Sie 'deposit' um Geld einzuzahlen.");
                System.out.println("Geben Sie 'logout' ein, um sich auszuloggen.");
                System.out.println("Geben Sie 'delete' ein, um ihr Konto zu loeschen.\n");
            }


            //Güter kaufen:
            if (eingabe.equals("buy")) {
                System.out.println("Welches Gut moechten Sie erwerben?");
                System.out.println("Es stehen folgende Gueter zur Auswahl: \n1.Reddit_Karma\n2.Valorant_Point\n3.Toilettenpapier\n4.Rapsoel\n5.NFT\n6.LSD\n7.Grafikkarten\n8.Discord_Nitro\n9.CSGO_Case_Keys\n10.Nieren\n");
                System.out.println("Bitte geben Sie den Namen den Gutes genau so wie genannt ein.");
                String item = s.nextLine();
                System.out.println("Wie viele moechten Sie erwerben?");
                int amount = s.nextInt();
                System.out.println(ui.buy(item, amount) + "\n");
            }


            //Güter verkaufen:
            if (eingabe.equals("sell")) {
                System.out.println("Welches Gut moechten Sie verkaufen?");
                String item = s.nextLine();
                System.out.println("Wie viele moechten Sie verkaufen?");
                int amount = s.nextInt();
                System.out.println(ui.sell(item, amount) + "\n");
            }


            //Preise aktualisieren
            if (eingabe.equals("refresh")) {
                System.out.println("Die Preise haben sich veraendert ;) \n");
                System.out.println(ui.refresh());
            }


            //Aktuellen Preise ausgeben
            if (eingabe.equals("prices")) {
                System.out.println(ui.prices());
            }


            //Kontoinformationen ausgeben
            if (eingabe.equals("info")) {
                System.out.println("Sie haben folgendes im Inventar: ");
                System.out.println(ui.UserInfo());
            }


            //Geld abheben
            if (eingabe.equals("withdraw")) {
                System.out.println("Wie viele Primogems sollen abgehoben werden?");
                double menge = s.nextDouble();
                System.out.println(ui.withdraw(menge) + "\n");
            }


            //Geld einzahlen
            if (eingabe.equals("deposit")) {
                System.out.println("Wie viele Primogems sollen eingezahlt werden?");
                double menge = s.nextDouble();
                System.out.println(ui.deposit(menge) + "\n");
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

