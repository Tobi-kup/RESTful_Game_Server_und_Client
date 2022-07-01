package com.example.restservice;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import org.json.simple.*;
import java.io.*;


/** UserInteraction ist die Klasse zur Erstellung der URIs, sie ist der Server des Projektes.
 * Sie erstellt die Pfade innerhalb des Spiels.
 * Der Client soll diesen dann folgen, um sich in dem Spiel zurecht zu finden und sein Konto zu verwalten.
 */

@RestController
public class UserInteraction {

    int aktuellerNutzer = 0;
    String[] users = new String[10];
    String Spieler1 = "";
    String Spieler2 = "";
    String letzterZug = "Spieler2";
    boolean lobbyFull = false;

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
     * @return einen String, der angibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
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
     * @return einen String, der angibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
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


    //bsp: http://localhost:8080/createSingleplayerGame
    /**
     * Erstellt ein Spiel zwischen dem Nutzer und dem Computer
     * @return einen String, der angibt, dass das Spiel gestartet wurde
     * @param name -Nötig, um auf die richtige Spieldatei zuzugreifen
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/createSingleplayerGame")
    public String createSingleplayerGame(@RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";
        letzterZug = "Spieler2";
        Spieler1 = name;
        gc.newGame(Spieler1);


        //Das boolean-Attribut "hasGame" in der JSON-Datei des aufrufenden Nutzers wird auf true gesetzt
        Object o = new JSONParser().parse(new FileReader(Spieler1 + ".json"));
        JSONObject js = (JSONObject) o;
        js.put("hasGame", true);

        FileWriter file = new FileWriter(Spieler1 + ".json");
        file.write(js.toJSONString());
        file.close();


        //Das Attribut "Spieler1" in der JSON-Datei für den Zustand des Spiels wird auf den aufrufenden Nutzer gesetzt
        //Und Das Attribut "Spieler2" auf "Computer"
        Object og = new JSONParser().parse(new FileReader(Spieler1 + "game.json"));
        JSONObject jsg = (JSONObject) og;
        jsg.put("Spieler1", Spieler1);
        jsg.put("Spieler2", "Computer");


        FileWriter fileg = new FileWriter(Spieler1 + "game.json");
        fileg.write(jsg.toJSONString());
        fileg.close();


        ausgabe = "Spiel erfolgreich gestartet. \nSie sind am Zug.";
        return ausgabe;
    }


    //bsp: http://localhost:8080/createMultiplayerGame
    /**
     * Erstellt ein Spiel mit dem Nutzer als Spieler 1 und setzt das Attribut "hasGame" innerhalb des Nutzkontos auf true.
     * @return einen String, der angibt, dass ein Spiel erstellt wurde und nun auf einen 2. Spieler gewartet wird
     * @param name -Nötig, um auf die richtige Spieldatei zuzugreifen
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/createMultiplayerGame")
    public String createMultiplayerGame(@RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";
        Spieler1 = name;
        gc.newGame(Spieler1);


        //Das boolean-Attribut "hasGame" in der JSON-Datei des aufrufenden Nutzers wird auf true gesetzt
        Object o = new JSONParser().parse(new FileReader(Spieler1 + ".json"));
        JSONObject js = (JSONObject) o;
        js.put("hasGame", true);

        FileWriter file = new FileWriter(  Spieler1 + ".json");
        file.write(js.toJSONString());
        file.close();


        //Das Attribut "Spieler1" in der JSON-Datei für den Zustand des Spiels wird auf den aufrufenden Nutzer gesetzt
        Object og = new JSONParser().parse(new FileReader( Spieler1 + "game.json"));
        JSONObject jsg = (JSONObject) og;
        jsg.put("Spieler1", Spieler1);


        FileWriter fileg = new FileWriter(  Spieler1 + "game.json");
        fileg.write(jsg.toJSONString());
        fileg.close();


        ausgabe = "Spiel erfolgreich gestartet, warte auf anderen Spieler...";
        return ausgabe;
    }


    //bsp: http://localhost:8080/join?name=Max
    /**
     * Es wird dem Spiel vom angegebenem Kontos beigetreten, dafür wird das Attribut Spieler 2, auf den aufrufenden Benutzer gelegt
     * und das Attribut "hasGame" innerhalb des aufrufenden Nutzkontos auf true gesetzt.
     * @param name -Nötig, um auf die richtige Spieldatei zuzugreifen
     * @return einen String, der angibt, ob der Beitritt erfolgreich war
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/join")
    public String join(@RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";
        boolean existiert = false;

        int anzahlExistenterNutzer = 0;
        for(int i=0; i<users.length; i++) {
            if(users[i] != null) anzahlExistenterNutzer++;
        }

        for(int i=0; i<anzahlExistenterNutzer; i++){
            if(users[i].equals(name)) existiert = true;
            }

        if(existiert == false) return "Es ist kein Spieler mit diesem Namen registiert.";


        //Nachschauen, ob der angefragte Nutzer ein Spiel erstellt hat
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;
        boolean hasGame = (boolean) js.get("hasGame");


        //Nachschauen, ob das Spiel bereits 2 Spieler enthält, also ob es schon läuft
        Object og = new JSONParser().parse(new FileReader(name + "game.json"));
        JSONObject jsg = (JSONObject) og;
        String player2 = (String) jsg.get("Spieler2");


        //Wenn das Spiel existiert und kein 2. Spieler da ist, wird der aktuelle Nutzer zum 2. Spieler
        if(hasGame == true && player2.equals("")){
            Spieler2 = users[aktuellerNutzer];
            lobbyFull = true;
            ausgabe = "Sie sind dem Spiel erfolreich beigetreten.";
            jsg.put("Spieler2", Spieler2);

            FileWriter fileg = new FileWriter(name + "game.json");
            fileg.write(jsg.toJSONString());
            fileg.close();

        }
        else ausgabe = "Dieser Spieler hat kein Spiel erstellt.";

        return ausgabe;
    }


    //bsp: http://localhost:8080/player1place?column=1&name=Max
    /**
     * Wirft eine Scheibe in die vom Nutzer angefragte Spalte, wenn er dran ist
     * @param column - Spalte, in die die Scheibe geworfen werden  soll
     * @param name -Nötig, um auf die richtige Spieldatei zuzugreifen
     * @return einen String, der angibt, ob der Zug erfolgreich war oder was falsch gelaufen ist
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/player1place")
    public String player1place(@RequestParam(value = "column") int column, @RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";

        if(letzterZug.equals("Spieler2")){
            Object o = new JSONParser().parse(new FileReader( name +"game.json"));
            JSONObject js = (JSONObject) o;

            if(column > 7) return "So viele Spalten gibt es nicht.";

            for(int i=6; i>=1; i--) {
                String zelle = (String) js.get("Feld" + i + column);
                if (zelle.equals(" ")) {
                    js.put("Feld" + i + column, "R");
                    ausgabe = "Erfolgreich abgelegt";
                    break;
                }
            }

            if(letzterZug.equals("Spieler2") && ausgabe.equals("")) return "Diese Spalte ist bereits komplett gefüllt";

            FileWriter file = new FileWriter(name + "game.json");
            file.write(js.toJSONString());
            file.close();

            letzterZug = "Spieler1";


        } else ausgabe = "Sie sind aktuell nicht dran";

        return ausgabe;
    }


    //bsp: http://localhost:8080/player2place?column=1&name=Max
    /**
     **
     * Wirft eine Scheibe in die vom Nutzer angefragte Spalte, wenn er dran ist
     * @param column - Spalte, in die die Scheibe geworfen werden  soll
     * @param name -Nötig, um auf die richtige Spieldatei zuzugreifen
     * @return einen String, der angibt, ob der Zug erfolgreich war oder was falsch gelaufen ist
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/player2place")
    public String player2place(@RequestParam(value = "column") int column, @RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";

        if(column > 7) return "So viele Spalten gibt es nicht.";

        if(letzterZug.equals("Spieler1")){
            Object o = new JSONParser().parse(new FileReader( name + "game.json"));
            JSONObject js = (JSONObject) o;

            for(int i=6; i>=1; i--) {
                String zelle = (String) js.get("Feld" + i + column);
                if (zelle.equals(" ")) {
                    js.put("Feld" + i + column, "Y");
                    ausgabe = "Erfolgreich abgelegt";
                    break;
                }
            }

            if(letzterZug.equals("Spieler1") && ausgabe.equals("")) return "Diese Spalte ist bereits komplett gefüllt";

            FileWriter file = new FileWriter(name + "game.json");
            file.write(js.toJSONString());
            file.close();

            letzterZug = "Spieler2";


        } else ausgabe = "Sie sind aktuell nicht dran";

        return ausgabe;
    }


    //bsp: http://localhost:8080/checkLastMove
    /**
     * Prüft welcher Spieler zuletzt seinen Zug gemacht hat und gibt aus welcher Spieler nun dran ist
     * @return einen String, der angibt, welcher Spieler gerade am Zug ist
     */
    @GetMapping("/checkLastMove")
    public String checkLastMove() {
        String ausgabe = "";

        if(letzterZug.equals("Spieler1")) ausgabe = "Spieler 2 ist am Zug";

        else ausgabe = "Spieler 1 ist am Zug";

        return ausgabe;
    }

    //bsp: http://localhost:8080/checkWinner?name=Max
    /**
     * Prüft ob 4 mal dieselbe Farbe entweder horizontal, vertikal oder diagonal im "Spielbrett" zu finden ist,
     * gibt diese aus, wenn es zutrifft, sonst einen leeren String
     * @param name -Nötig, um auf die richtige Spieldatei zuzugreifen
     * @return einen String, der entweder leer ist, wenn noch kein Gewinner feststeht, oder je nachdem 'R' oder 'Y' enthält, wenn diese 4 mal dieselbe Farbe auftaucht
     */
    @GetMapping("/checkWinner")
    public String checkWinner(@RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = " ";

        Object o = new JSONParser().parse(new FileReader( name + "game.json"));
        JSONObject js = (JSONObject) o;

        //Horizontal prüfen
        //Zeilen durchlaufen
        for (int i=1; i<=6; i++) {

            //spalten durchlaufen
            for (int j=1; j<=4; j++) {

                String zelleJ = (String) js.get("Feld" + i + j);
                String zelleJPlus1 = (String) js.get("Feld" + i + (j+1));
                String zelleJPlus2 = (String) js.get("Feld" + i + (j+2));
                String zelleJPlus3 = (String) js.get("Feld" + i + (j+3));

                if (       (!zelleJ.equals(" "))
                        && (!zelleJPlus1.equals(" "))
                        && (!zelleJPlus2.equals(" "))
                        && (!zelleJPlus3.equals(" "))
                        && (zelleJ.equals(zelleJPlus1))
                        && (zelleJPlus1.equals(zelleJPlus2))
                        && (zelleJPlus2.equals(zelleJPlus3))) {

                    ausgabe = zelleJ;
                    return ausgabe;
                }
            }
        }


        //Vertikal prüfen
        //Spalten durchlaufen
        for (int i=1; i<=7; i++){

            //Zeilen durchlaufen
            for (int j=1; j<=3; j++) {

                String zelleI = (String) js.get("Feld" + j + i);
                String zelleIPlus1 = (String) js.get("Feld" + (j+1) + i);
                String zelleIPlus2 = (String) js.get("Feld" + (j+2) + i);
                String zelleIPlus3 = (String) js.get("Feld" + (j+3) + i);

                if(        (!zelleI.equals(" "))
                        && (!zelleIPlus1.equals(" "))
                        && (!zelleIPlus2.equals(" "))
                        && (!zelleIPlus3.equals(" "))
                        && (zelleI.equals(zelleIPlus1))
                        && (zelleIPlus1.equals(zelleIPlus2))
                        && (zelleIPlus2.equals(zelleIPlus3))) {

                    ausgabe = zelleI;
                    return ausgabe;
                }
            }
        }



        //Diagonal links oben bis rechts unten prüfen
        //Zeilen bis zur 3. Zeile durchlaufen
        for (int i=1; i<=3; i++){

            //Spalten bis zur 3. Spalte durchlaufen
            for (int j=1; j<=3; j++) {

                String zelleIJ = (String) js.get("Feld" + i + j);
                String zelleIPlus1JPlus1 = (String) js.get("Feld" + (i+1) + (j+1));
                String zelleIPlus2JPlus2 = (String) js.get("Feld" + (i+2) + (j+2));
                String zelleIPlus3JPlus3 = (String) js.get("Feld" + (i+3) + (j+3));

                if(        (!zelleIJ.equals(" "))
                        && (!zelleIPlus1JPlus1.equals(" "))
                        && (!zelleIPlus2JPlus2.equals(" "))
                        && (!zelleIPlus3JPlus3.equals(" "))
                        && (zelleIJ.equals(zelleIPlus1JPlus1))
                        && (zelleIPlus1JPlus1.equals(zelleIPlus2JPlus2))
                        && (zelleIPlus2JPlus2.equals(zelleIPlus3JPlus3))) {

                    ausgabe = zelleIJ;
                    return ausgabe;
                }
            }
        }


        //Diagonal rechts oben bis links unten prüfen
        //Zeilen bis zur 3. Zeile durchlaufen
        for (int i=1; i<=3; i++){

            //Spalten von der 4. zu der 7. Spalte durchlaufen
            for (int j=4; j<=7; j++) {

                String zelleIJ = (String) js.get("Feld" + i + j);
                String zelleIPlus1JMinus1 = (String) js.get("Feld" + (i+1) + (j-1));
                String zelleIPlus2JMinus2 = (String) js.get("Feld" + (i+2) + (j-2));
                String zelleIPlus3JMinus3 = (String) js.get("Feld" + (i+3) + (j-3));

                if(        (!zelleIJ.equals(" "))
                        && (!zelleIPlus1JMinus1.equals(" "))
                        && (!zelleIPlus2JMinus2.equals(" "))
                        && (!zelleIPlus3JMinus3.equals(" "))
                        && (zelleIJ.equals(zelleIPlus1JMinus1))
                        && (zelleIPlus1JMinus1.equals(zelleIPlus2JMinus2))
                        && (zelleIPlus2JMinus2.equals(zelleIPlus3JMinus3))) {

                    ausgabe = zelleIJ;
                    return ausgabe;
                }
            }
        }
        return ausgabe;
    }


    //bsp: http://localhost:8080/visualize
    /**
     * Gibt das aktuelle "Spielbrett" formatiert in einem String aus
     * @return einen String, der den Zustand des aktuellen "Spielbretts" enthält
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/visualize")
    public String visualize(@RequestParam(value = "name") String name) throws IOException, ParseException {
        String ausgabe = "";

        Object o = new JSONParser().parse(new FileReader( name + "game.json"));
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


    //bsp: http://localhost:8080/checkIfStartable
    /**
     * Prüft, ob ein 2. Spieler dem selbst kreierten Spiel beigetreten ist
     * @return einen String, der angibt, ob ein 2. Spieler beigetreten ist
     */
    @GetMapping("/checkIfStartable")
    public String checkIfStartable() {
        String ausgabe = "";

        if(lobbyFull == true) ausgabe = "Das Spiel kann gestartet werden";

        else ausgabe = "Es ist noch kein 2. Spieler beigetreten";

        return ausgabe;
    }


    //bsp: http://localhost:8080/delete
    /**
     * Löscht das Konto des angemeldeten Benutzers, indem jede Variable auf den leeren String gesetzt wird.
     * @return einen String, der angibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
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