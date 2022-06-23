package com.example.restservice;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import org.json.simple.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.lang.Math.*;


/** UserInteraction ist die Klasse zur Erstellung der URIs, sie ist der Server des Projektes.
 * Sie erstellt die Pfade innerhalb des Marktplatzes.
 * Der Client soll diesen dann folgen, um sich auf dem Server zurechzufinden.
 */

@RestController
public class UserInteraction {

    int aktuellerNutzer = 0;
    String[] users = new String[10];
    String Spieler1 = "";
    String Spieler2 = "";
    int aktuellDran = 1;
    String letzterZug = "Spieler2";

    GameCreation gc = new GameCreation();
    UserCreation uc = new UserCreation();

    public UserInteraction(){}


    //bsp: http://localhost:8080/registeredUsers
    /**
     * Listet alle registrierten Nutzer auf.
     * @return einen String, der alle registrierten Nutzer auflistet
     */
    @GetMapping("/registeredUsers")
    public String alle() {
        String ausgabe = "Folgend eine Auflistung aller registrierter Nutzer: ";
        for (int i = 0; i < users.length; i++) {
            if(users[i] != null) {
                ausgabe += "\n" + users[i];
            }
        }
        return ausgabe;
    }


    //bsp: http://localhost:8080/signup?name=Max&password=MPasswort
    /**
     * Fordert den Nutzer auf seine gewünschten Zugangsdaten zu wählen und somit einen Account zu erstellen,
     * wenn dieser Nutzer nicht bereits existiert, und noch genügend Platz für einen weiteren vorhanden ist, wird der neue Nutzer in das Array mitaufgenommen
     * und eine neue JSON Datei mit den Daten des Nutzers kreiert.
     * @param name - Name des Nutzerprofils vom Typ String
     * @param password - Passwort des Nutzerprofils vom Typ String
     * @return einen String, der ausgibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
     * @throws IOException
     */
    @GetMapping("/signup")
    public String signup(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password) throws IOException {
        String ausgabe = "";

        if(users[0] == null) {                        //bisher existiert kein User
            users[0] = name;
            uc.newUser(name, password);
            ausgabe = "Erfolgreich registiert.";
            return ausgabe;
        }

        int anzahlExistenterNutzer = 0;
        for(int i=0; i<users.length; i++) {
            if(users[i] != null){ anzahlExistenterNutzer++; }
        }

        for(int i=0; i<anzahlExistenterNutzer; i++) {  //Array wird durchlaufen
            if (users[i].equals(name)) {               //wenn dem Namen bereits einem Konto innerhalb des Arrays zugeorndet ist
                ausgabe = "User existiert bereits.";  //wird dies so ausgegeben
                return ausgabe;
            }
        }

        int j = 0;
        while (j < users.length && users[j] != null) { j++; }  //Array wird durchlaufen

        if(j < users.length) {                         //wenn die Zählvariable kleiner aus die Länge des Array ist
            users[j] = name;                           //wird der neue Nutzer in das Array mitaufgenommen
            uc.newUser(name, password);
            ausgabe = "Erfolgreich registiert.";
        } else ausgabe = "Die maximale Anzahl an registrierten Nutzern ist bereits erreicht.";
        return ausgabe;
    }


    //bsp: http://localhost:8080/login?name=Max&password=MPasswort
    /**
     * Fragt den Nutzer nach seinen Nutzerdaten, um ihm den Zugang zu gewähren.
     * @param name - Name des Nutzerprofils vom Typ String
     * @param password - Passwort des Nutzerprofils vom Typ String
     * @return einen String, der ausgibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password) throws IOException, ParseException {
        String ausgabe = "";
        boolean thisUserExists = false;
        if(users[0] == null){ return "Es ist noch kein Nutzer registriert."; }

        int anzahlExistenterNutzer = 0;
        for(int i=0; i<users.length; i++) {
            if(users[i] != null){ anzahlExistenterNutzer++; }
        }

        for(int i=0; i<anzahlExistenterNutzer; i++){
            if(users[i].equals(name)){
                thisUserExists = true;
            }
        }

        if(thisUserExists == true) {
            Object o = new JSONParser().parse(new FileReader(name + ".json"));
            JSONObject js = (JSONObject) o;
            String pw = (String) js.get("password");

            for (int i = 0; i < anzahlExistenterNutzer; i++) {
                if (users[i].equals(name) && pw.equals(password)) {
                    aktuellerNutzer = i;
                    ausgabe = "Login erfolgreich." ;
                    return ausgabe;

                } else if (users[i].equals(name)) {
                    ausgabe = "Passwort ist falsch.";
                    return ausgabe;
                }
            }
        } else ausgabe = "Dieser Nutzer existiert nicht.";

        return ausgabe;
    }


    //bsp: http://localhost:8080/createMultiplayerGame
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/createMultiplayerGame")
    public String createMultiplayerGame() throws IOException, ParseException {
        String ausgabe = "";
        Spieler1 = users[aktuellerNutzer];
        gc.newGame();


        //Das boolean-Attribut "hasGame" in der JSON-Datei des aufrufenden Nutzers wird auf true gesetzt
        Object o = new JSONParser().parse(new FileReader(Spieler1 + ".json"));
        JSONObject js = (JSONObject) o;
        js.put("hasGame", true);

        FileWriter file = new FileWriter(  Spieler1 + ".json");
        file.write(js.toJSONString());
        file.close();


        //Das Attribut "Spieler1" in der JSON-Datei für den Zustand des Spiels wird auf den aufrufenden Nutzer gesetzt
        Object og = new JSONParser().parse(new FileReader( "game.json"));
        JSONObject jsg = (JSONObject) og;
        jsg.put("Spieler1", Spieler1);


        FileWriter fileg = new FileWriter(  "game.json");
        fileg.write(jsg.toJSONString());
        fileg.close();


        ausgabe = "Spiel erfolgreich gestartet, warte auf anderen Spieler...";
        return ausgabe;
    }


    //bsp: http://localhost:8080/createSingleplayerGame
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/createSingleplayerGame")
    public String createSingleplayerGame() throws IOException, ParseException {
        String ausgabe = "";
        Spieler1 = users[aktuellerNutzer];
        gc.newGame();


        //Das boolean-Attribut "hasGame" in der JSON-Datei des aufrufenden Nutzers wird auf true gesetzt
        Object o = new JSONParser().parse(new FileReader(Spieler1 + ".json"));
        JSONObject js = (JSONObject) o;
        js.put("hasGame", true);

        FileWriter file = new FileWriter(  Spieler1 + ".json");
        file.write(js.toJSONString());
        file.close();


        //Das Attribut "Spieler1" in der JSON-Datei für den Zustand des Spiels wird auf den aufrufenden Nutzer gesetzt
        //Und Das Attribut "Spieler2" auf "Computer"
        Object og = new JSONParser().parse(new FileReader( "game.json"));
        JSONObject jsg = (JSONObject) og;
        jsg.put("Spieler1", Spieler1);
        jsg.put("Spieler2", "Computer");


        FileWriter fileg = new FileWriter(  "game.json");
        fileg.write(jsg.toJSONString());
        fileg.close();


        ausgabe = "Spiel erfolgreich gestartet.";
        return ausgabe;
    }


    //bsp: http://localhost:8080/join?name=Max
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/join")
    public String join(@RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";


        //Nachschauen, ob der angefragte Nutzer ein Spiel erstellt hat
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;
        boolean hasGame = (boolean) js.get("hasGame");


        //Nachschauen, ob das Spiel bereits 2 Spieler enthält, also ob es schon läuft
        Object og = new JSONParser().parse(new FileReader("game.json"));
        JSONObject jsg = (JSONObject) og;
        String player2 = (String) jsg.get("Spieler2");


        //Wenn das Spiel existiert und kein 2. Spieler da ist, wird der aktuelle Nutzer zum 2. Spieler
        if(hasGame == true && player2.equals("")){
            Spieler2 = users[aktuellerNutzer];
            ausgabe = "Sie sind dem Spiel erfolreich beigetreten";
            jsg.put("Spieler2", Spieler2);

            FileWriter fileg = new FileWriter("game.json");
            fileg.write(jsg.toJSONString());
            fileg.close();

        }
        else ausgabe = "Dieser Spieler hat kein Spiel erstellt.";

        return ausgabe;
    }


    //bsp: http://localhost:8080/player1place?column=1
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/player1place")
    public String player1place(@RequestParam(value = "column") int column) throws IOException, ParseException {
        String ausgabe = "";

        if(letzterZug.equals("Spieler2")){
            Object o = new JSONParser().parse(new FileReader( "game.json"));
            JSONObject js = (JSONObject) o;

            for(int i=6; i>=1; i--) {
                String zelle = (String) js.get("Feld" + i + column);
                if (zelle.equals(" ")) {
                    js.put("Feld" + i + column, "R");
                    ausgabe = "Erfolgreich abgelegt";
                    break;
                }
            }

            FileWriter file = new FileWriter("game.json");
            file.write(js.toJSONString());
            file.close();

            letzterZug = "Spieler1";

        } else if(letzterZug.equals("Spieler2") && ausgabe == ""){
            ausgabe = "Diese Spalte ist bereits komplett gefüllt";

        } else ausgabe = "Sie sind aktuell nicht dran";

        return ausgabe;
    }


    //bsp: http://localhost:8080/player2place?column=1
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/player2place")
    public String player2place(@RequestParam(value = "column") int column) throws IOException, ParseException {
        String ausgabe = "";

        if(letzterZug.equals("Spieler1")){
            Object o = new JSONParser().parse(new FileReader( "game.json"));
            JSONObject js = (JSONObject) o;

            for(int i=6; i>=1; i--) {
                String zelle = (String) js.get("Feld" + i + column);
                if (zelle.equals(" ")) {
                    js.put("Feld" + i + column, "Y");
                    ausgabe = "Erfolgreich abgelegt";
                    break;
                }
            }

            FileWriter file = new FileWriter("game.json");
            file.write(js.toJSONString());
            file.close();

            letzterZug = "Spieler2";

        } else if(letzterZug.equals("Spieler1") && ausgabe == ""){
            ausgabe = "Diese Spalte ist bereits komplett gefüllt";

        } else ausgabe = "Sie sind aktuell nicht dran";

        return ausgabe;
    }


    //bsp: http://localhost:8080/checkLastMove
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/checkLastMove")
    public String checkLastMove() throws IOException, ParseException {
        String ausgabe = "";

        if(letzterZug.equals("Spieler1")) ausgabe = "Spieler 2 ist am Zug";

        else ausgabe = "Spieler 1 ist am Zug";

        return ausgabe;
    }


    //bsp: http://localhost:8080/visualize
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/visualize")
    public String visualize() throws IOException, ParseException {
        String ausgabe = "";

        Object o = new JSONParser().parse(new FileReader( "game.json"));
        JSONObject js = (JSONObject) o;

        String Feld11 = (String) js.get("Feld11");
        String Feld12 = (String) js.get("Feld12");
        String Feld13 = (String) js.get("Feld13");
        String Feld14 = (String) js.get("Feld14");
        String Feld15 = (String) js.get("Feld15");
        String Feld16 = (String) js.get("Feld16");
        String Feld17 = (String) js.get("Feld17");

        String Feld21 = (String) js.get("Feld21");
        String Feld22 = (String) js.get("Feld22");
        String Feld23 = (String) js.get("Feld23");
        String Feld24 = (String) js.get("Feld24");
        String Feld25 = (String) js.get("Feld25");
        String Feld26 = (String) js.get("Feld26");
        String Feld27 = (String) js.get("Feld27");

        String Feld31 = (String) js.get("Feld31");
        String Feld32 = (String) js.get("Feld32");
        String Feld33 = (String) js.get("Feld33");
        String Feld34 = (String) js.get("Feld34");
        String Feld35 = (String) js.get("Feld35");
        String Feld36 = (String) js.get("Feld36");
        String Feld37 = (String) js.get("Feld37");

        String Feld41 = (String) js.get("Feld41");
        String Feld42 = (String) js.get("Feld42");
        String Feld43 = (String) js.get("Feld43");
        String Feld44 = (String) js.get("Feld44");
        String Feld45 = (String) js.get("Feld45");
        String Feld46 = (String) js.get("Feld46");
        String Feld47 = (String) js.get("Feld47");

        String Feld51 = (String) js.get("Feld51");
        String Feld52 = (String) js.get("Feld52");
        String Feld53 = (String) js.get("Feld53");
        String Feld54 = (String) js.get("Feld54");
        String Feld55 = (String) js.get("Feld55");
        String Feld56 = (String) js.get("Feld56");
        String Feld57 = (String) js.get("Feld57");

        String Feld61 = (String) js.get("Feld61");
        String Feld62 = (String) js.get("Feld62");
        String Feld63 = (String) js.get("Feld63");
        String Feld64 = (String) js.get("Feld64");
        String Feld65 = (String) js.get("Feld65");
        String Feld66 = (String) js.get("Feld66");
        String Feld67 = (String) js.get("Feld67");


        ausgabe = "|" + Feld11 + "|" + Feld12 + "|" + Feld13 + "|" + Feld14 + "|" + Feld15 + "|" + Feld16 + "|" + Feld17 + "|\n"  +
                  "|" + Feld21 + "|" + Feld22 + "|" + Feld23 + "|" + Feld24 + "|" + Feld25 + "|" + Feld26 + "|" + Feld27 + "|\n"  +
                  "|" + Feld31 + "|" + Feld32 + "|" + Feld33 + "|" + Feld34 + "|" + Feld35 + "|" + Feld36 + "|" + Feld37 + "|\n"  +
                  "|" + Feld41 + "|" + Feld42 + "|" + Feld43 + "|" + Feld44 + "|" + Feld45 + "|" + Feld46 + "|" + Feld47 + "|\n"  +
                  "|" + Feld51 + "|" + Feld52 + "|" + Feld53 + "|" + Feld54 + "|" + Feld55 + "|" + Feld56 + "|" + Feld57 + "|\n"  +
                  "|" + Feld61 + "|" + Feld62 + "|" + Feld63 + "|" + Feld64 + "|" + Feld65 + "|" + Feld66 + "|" + Feld67 + "|\n";


        return ausgabe;
    }


    //bsp: http://localhost:8080/delete
    /**
     * Löscht das Konto des angemeldeten Benutzers, indem jede Variable auf den leeren String gesetzt wird.
     * @return einen String, der ausgibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/delete")
    public String delete() throws IOException, ParseException {
        String ausgabe = "";

        if(users[aktuellerNutzer] == null){
            ausgabe = "Sie müssen angemeldet sein, um ihr Konto zu löschen.";
            return ausgabe;
        }

        String name = users[aktuellerNutzer];
        users[aktuellerNutzer] = null;

        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;
        js.put("name", "");
        js.put("password", "");
        js.put("hasGame", "");

        FileWriter file = new FileWriter(  name + ".json");
        file.write(js.toJSONString());
        file.close();

        ausgabe = "Konto erfolgreich geloescht";

        return ausgabe;
    }


    //bsp: http://localhost:8080/shutdown
    /**
     *Schließt den Server.
     */
    @GetMapping("/shutdown")
    public void shutdown(){
        System.exit(0);
    }
}