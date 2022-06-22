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
    String aktuellDran = "";
    String Spieler1 = "";
    String Spieler2 = "";

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
        aktuellDran = users[aktuellerNutzer];
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
        aktuellDran = users[aktuellerNutzer];
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


//    //bsp: http://localhost:8080/player1place
//    /**
//     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
//     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
//     * @throws IOException
//     * @throws ParseException
//     */
//    @GetMapping("/player1place")
//    public String player1place() throws IOException, ParseException {
//        String ausgabe = "";
//
//        if(aktuellDran == 1){
//
//        }
//        String name = users[aktuellerNutzer];
//        Object o = new JSONParser().parse(new FileReader(name + ".json"));
//        JSONObject js = (JSONObject) o;
//
//
//        return ausgabe;
//    }


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
        js.put("balance", "");
        js.put("RedditKarma", "");
        js.put("VpPoints", "");
        js.put("Toilettenpapier", "");
        js.put("Rapsoel", "");
        js.put("NFTs", "");
        js.put("LSD", "");
        js.put("Grafikkarten", "");
        js.put("Nieren", "");
        js.put("DiscordNitro", "");
        js.put("CsgoCaseKeys", "");

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