package unused;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.*;

/**
 * Klasse, die den Client, des Projektes darstellt.
 * Sie ruft die im Server generierten URIs auf und arbeitet mit der Ausgabe dieser.
 */

public class ClientUeberURL {

    public static void main(String[] args) throws IOException, ParseException {

        /**
         * 2 Attribute vom Typ String, zur späteren Speicherung vom Namen und Password des Nutzkontos
         * Instanz der Klasser Scanner für die Terminaleingaben
         * 2 Attribute vom Typ boolean angemeldet und registered für den Programmdurchlauf
         * Instanz der Klasse RestTemplatem, um die Daten Ausgabe des Servers zu erhalten
         */
        String name;
        String password;
        Scanner s = new Scanner(System.in);
        boolean angemeldet = false;
        boolean registered = false;
        String ausgabe = "";
        RestTemplate rt = new RestTemplate();


        String eingabe = "";
        /**
         * Erste while Schleife: zuständig für das Anmelden und Registrieren
         * Sobald der Benutzer registriert und angemeldet ist, wird in die zweite while Schleife übergegangen
         */
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

        System.out.println("Geben sie 'help' ein, um alle akzeptierten Befehle anzuzeigen.\n");

        /**
         * zweite Schleife und Hauptteil des Programms, checkt Eingabe und führt die vom Benutzer gewollten Befehle aus
         */
        while (angemeldet == true) {

            eingabe = s.nextLine();


            /**
             * Ausgabe um dem Benutzer alle möglichen Befehle zu zeigen
             */
            if (eingabe.equals("help")) {
                System.out.println("");
                System.out.println("Moechten Sie etwas kaufen, etwas verkaufen, sich Informationen ueber ihr Konto ausgeben lassen, die Preise aktualisieren, Geld abzuheben, Geld einzahlen oder sich ausloggen?");
                System.out.println("Geben Sie 'buy' ein, um Gueter zu erwerben.");
                System.out.println("Geben Sie 'sell' ein, um Gueter zu verkaufen.");
                System.out.println("Geben Sie 'info' ein, um Informationen ueber ihr Konto zu erhalten.");
                System.out.println("Geben Sie 'refresh' ein, um die Preise zu aktualisieren.");
                System.out.println("Geben Sie 'prices' ein, um die die aktuellen Preise ausgegeben zu bekommen.");
                System.out.println("Geben Sie 'withdraw' ein, um Geld abzuheben.");
                System.out.println("Geben Sie 'deposit' ein, um Geld einzuzahlen.");
                System.out.println("Geben Sie 'logout' ein, um sich auszuloggen.");
                System.out.println("Geben Sie 'delete' ein, um ihr Konto zu loeschen.\n");
            }


            /**
             * Kauft Güter mit der buy Methode in UserInteraction durch Aufrufen der URI
             * checkt die Eingabe von item und anzahl und ruft die URI mit den entsprechenden Werten auf
             */
            else if (eingabe.equals("buy")) {
                System.out.println("Welches Gut moechten Sie erwerben?");
                System.out.println("Es stehen folgende Gueter zur Auswahl: \n1.Reddit_Karma\n2.Valorant_Point\n3.Toilettenpapier\n4.Rapsoel\n5.NFT\n6.LSD\n7.Grafikkarten\n8.Discord_Nitro\n9.CSGO_Case_Keys\n10.Nieren\n");
                System.out.println("Bitte geben Sie den Namen den Gutes genau so wie genannt ein.");
                String item = s.nextLine();
                System.out.println("Wie viele moechten Sie erwerben?");
                int amount = s.nextInt();
                System.out.println(rt.getForObject("http://localhost:8080/buy?item="+item+"&amount="+amount+"", String.class) + "\n");
            }


            /**
             * Verkauft Güter mit der sell Methode in UserInteraction durch Aufrufen der URI
             * checkt die Eingabe von item und anzahl und ruft die URI mit den entsprechenden Werten auf
             */
            else if (eingabe.equals("sell")) {
                System.out.println("Welches Gut moechten Sie verkaufen?");
                String item = s.nextLine();
                System.out.println("Wie viele moechten Sie verkaufen?");
                int amount = s.nextInt();
                System.out.println(rt.getForObject("http://localhost:8080/sell?item="+item+"&amount="+amount+"", String.class) + "\n");
            }


            /**
             * aktualisiert die Güterpreise mit der refresh Methode in UserInteraction durch Aufrufen der URI
             */
            else if (eingabe.equals("refresh")) {
                System.out.println("Die Preise haben sich veraendert ;) \n");
                System.out.println(rt.getForObject("http://localhost:8080/refresh", String.class));
            }


            /**
             * gibt die aktuellen Güterpreise mit der prices Methode in UserInteraction durch Aufrufen der URI aus
             */
            else if (eingabe.equals("prices")) {
                System.out.println("Dies sind die aktuellen Gueterpreise:\n");
                System.out.println(rt.getForObject("http://localhost:8080/prices", String.class));
            }


            /**
             * Gibt die Kontoinformationen des Benutzers mit der info Methode in UserInteraction durch Aufrufen der URI aus
             */
            else if (eingabe.equals("info")) {
                System.out.println(rt.getForObject("http://localhost:8080/info", String.class));
            }


            /**
             * Zieht den eingegebenen Wert von dem Kontostand des Benutzerkontos mit der withdraw Methode in UserInteraction durch aufrufen der URL ab
             * checkt die Eingabe von menge und ruft die URI mit dem entsprechenden Wert auf
             */
            else if (eingabe.equals("withdraw")) {
                System.out.println("Wie viele Primogems sollen abgehoben werden?");
                double menge = s.nextDouble();
                System.out.println(rt.getForObject("http://localhost:8080/withdraw?amount="+menge+"", String.class) + "\n");
            }


            /**
             * Zahlt den eingegebenen Wert auf den Kontostand des Benutzerkontos mit der deposit Methode in UserInteraction durch aufrufen der URL ein
             * checkt die Eingabe von menge und ruft die URI mit dem entsprechenden Wert auf
             */
            else if (eingabe.equals("deposit")) {
                System.out.println("Wie viele Primogems sollen eingezahlt werden?");
                double menge = s.nextDouble();
                System.out.println(rt.getForObject("http://localhost:8080/deposit?amount="+menge+"", String.class) + "\n");
            }


            /**
             * beendet die aktuelle Schleife und somit den Client, wenn der Benutzer "logout" eingibt
             */
            else if (eingabe.equals("logout")) {
                angemeldet = false;
            }


            /**
             * Ruft die URI für das Löschen des aktuell angemeldeten Nutzerkontos auf und die URI für das Schließen des Servers
             * und beendet die aktuelle Schleife und somit den Client
             */
            else if (eingabe.equals("delete")) {
                System.out.println(rt.getForObject("http://localhost:8080/delete", String.class));
                angemeldet = false;
                System.out.println(rt.getForObject("http://localhost:8080/shutdown", String.class));
            

            } else {
                System.out.println("Ungueltige Eingabe");
            }
        }

    }
}
