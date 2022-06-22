package unused;
import com.example.restservice.UserCreation;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import org.json.simple.*;
import unused.Randomwalk;

import java.io.*;


/** UserInteractionMitJSON ist die Klasse zur Erstellung der URIs, sie ist der Server des Projektes.
 * Sie erstellt die Pfade innerhalb des Marktplatzes.
 * Der Client soll diesen dann folgen, um sich auf dem Server zurechzufinden.
 */

@RestController
public class UserInteractionMitJSON {

    /**
     * 10 Attribute vom Typ double, die die Preise der Güter festlegen
     * Sie können mit einer Methode beim Betrieb des Servers verändert werden
     * 1 Attribut vom Typ int, das die Stelle des aktuell angemeldeten Nutzers innerhalb des Arrays speichert
     * 1 Arrays vom Typ String, das die Namen der registrierten Nutzer speichert
     * 1 Instanz der Klasse UserCreation, die bei der Registrierung verwendet wird, um neue Nutzer zu erstellen
     */
    public double Reddit_Karma_Preis = 0.1;
    public double Valorant_Points_Preis = 0.01;
    public double Toilettenpapier_Preis = 1;
    public double Rapsoel_Preis = 5;
    public double NFT_Preis = 2000;
    public double LSD_Preis = 20;
    public double Grafikkarten_Preis = 500;
    public double Discord_Nitro_Preis = 10;
    public double CSGO_Case_Key_Preis = 15;
    public double Nieren_Preis = 37000;

    int aktuellerNutzer = 0;
    String[] users = new String[10];

    UserCreation uc = new UserCreation();

    public UserInteractionMitJSON(){}


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


    //bsp: http://localhost:8080/info
    /**
     * Gibt den Kontostand des Nutzers und jeweils die Menge der Güter, die er besitzt, aus, wenn ein Nutzer angemeldet ist
     * @return einen String, der den Kontostand und die Güter des Nutzes ausgibt, oder, dass kein Nutzer angemeldet ist.
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/info")
    public String UserInfo() throws IOException, ParseException {
        String ausgabe = "";

        String name = users[aktuellerNutzer];
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;

        double Primogems = (double) js.get("balance");
        double RedditKarma = (double) js.get("RedditKarma");
        double VP_Points = (double) js.get("VpPoints");
        double Toilettenpapier = (double) js.get("Toilettenpapier");
        double Rapsoel = (double) js.get("Rapsoel");
        double NFTs = (double) js.get("NFTs");
        double LSD = (double) js.get("LSD");
        double Grafikkarten = (double) js.get("Grafikkarten");
        double Discord_Nitro = (double) js.get("DiscordNitro");
        double Nieren = (double) js.get("Nieren");
        double CSGO_Case_Keys = (double) js.get("CsgoCaseKeys");

        ausgabe = "Primogems: " + Primogems +
                "\nGueter:\n" +
                "Reddit_Karma: " + RedditKarma +
                "\nVP_Points: " + VP_Points +
                "\nToilettenpapier: " + Toilettenpapier +
                "\nRapsoel: " + Rapsoel +
                "\nNFTs: " + NFTs +
                "\nLSD: " + LSD +
                "\nGrafikkarten: " + Grafikkarten +
                "\nDiscord_Nitro: " + Discord_Nitro +
                "\nNieren: " + Nieren +
                "\nCSGO_Case_Keys: " + CSGO_Case_Keys + "\n";

        return ausgabe;
    }


    //bsp: http://localhost:8080/buy?item=Reddit_Karma&amount=1
    /**
     * Kauft angegebene Menge des angegebenen Gutes für den aktuellen Marktpreis auf Nutzeranfrag,
     * erhöht dafür entsprechend die Menge des Gutes innerhalb des Nutzerkontos und vermindert dessen Kontostand.
     * @param item - Gut, das gekauft werden soll
     * @param amount - Menge, die gekauft werden soll
     * @return einen String, der ausgibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/buy")
    public String buy(@RequestParam(value = "item") String item, @RequestParam(value = "amount") int amount) throws IOException, ParseException {
        String ausgabe = "";

        String name = users[aktuellerNutzer];
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;

        double Primogems = (double) js.get("balance");
        double RedditKarma = (double) js.get("RedditKarma");
        double VP_Points = (double) js.get("VpPoints");
        double Toilettenpapier = (double) js.get("Toilettenpapier");
        double Rapsoel = (double) js.get("Rapsoel");
        double NFTs = (double) js.get("NFTs");
        double LSD = (double) js.get("LSD");
        double Grafikkarten = (double) js.get("Grafikkarten");
        double Discord_Nitro = (double) js.get("DiscordNitro");
        double Nieren = (double) js.get("Nieren");
        double CSGO_Case_Keys = (double) js.get("CsgoCaseKeys");

        switch(item) {
            case "Reddit_Karma":
                if(Primogems - (amount * Reddit_Karma_Preis)>=0) {
                    RedditKarma += amount;
                    Primogems -= amount*Reddit_Karma_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "Valorant_Points":
                if(Primogems - (amount * Valorant_Points_Preis)>=0) {
                    VP_Points += amount;
                    Primogems -= amount*Valorant_Points_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "Toilettenpapier":
                if(Primogems - (amount * Toilettenpapier_Preis)>=0) {
                    Toilettenpapier += amount;
                    Primogems -= amount*Toilettenpapier_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "Rapsoel":
                if(Primogems - (amount * Rapsoel_Preis)>=0) {
                    Rapsoel += amount;
                    Primogems -= amount*Rapsoel_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "NFT":
                if(Primogems - (amount * NFT_Preis)>=0) {
                    NFTs += amount;
                    Primogems -= amount*NFT_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "LSD":
                if(Primogems - (amount * LSD_Preis)>=0) {
                    LSD += amount;
                    Primogems -= amount*LSD_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "Grafikkarten":
                if(Primogems - (amount * Grafikkarten_Preis)>=0) {
                    Grafikkarten += amount;
                    Primogems -= amount*Grafikkarten_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "Discord_Nitro":
                if(Primogems - (amount * Discord_Nitro_Preis)>=0) {
                    Discord_Nitro += amount;
                    Primogems -= amount*Discord_Nitro_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "CSGO_Case_Key":
                if(Primogems - (amount * CSGO_Case_Key_Preis)>=0) {
                    CSGO_Case_Keys += amount;
                    Primogems -= amount*CSGO_Case_Key_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            case "Nieren":
                if(Primogems - (amount * Nieren_Preis)>=0) {
                    Nieren += amount;
                    Primogems -= amount*Nieren_Preis;
                    ausgabe = "Kauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie haben nicht genug Primogems.";
                break;

            default:
                ausgabe = "Ungueltige Eingabe, dieses Gut wird nicht angeboten.";
        }


        js.put("balance", Primogems);
        js.put("RedditKarma", RedditKarma);
        js.put("VpPoints", VP_Points);
        js.put("Toilettenpapier", Toilettenpapier);
        js.put("Rapsoel", Rapsoel);
        js.put("NFTs", NFTs);
        js.put("LSD", LSD);
        js.put("Grafikkarten", Grafikkarten);
        js.put("Nieren", Nieren);
        js.put("DiscordNitro", Discord_Nitro);
        js.put("CsgoCaseKeys", CSGO_Case_Keys);

        FileWriter file = new FileWriter(  name + ".json");
        file.write(js.toJSONString());
        file.close();

        return ausgabe;
    }


    //bsp: http://localhost:8080/sell?item=Valorant_Points&amount=1
    /**
     * Verkauft angegebene Menge des angegebenen Gutes für den aktuellen Marktpreis auf Nutzeranfrage,
     * vermindert dafür entsprechend die Menge des Gutes innerhalb des Nutzerkontos und erhöht dessen Kontostand.
     * @param item - Gut, das verkauft werden soll
     * @param amount - Menge, die verkauft werden soll
     * @return einen String, der ausgibt, welcher Fehler aufgetreten ist, oder ob es funktioniert hat
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/sell")
    public String sell(@RequestParam(value = "item") String item, @RequestParam(value = "amount") int amount) throws IOException, ParseException {
        String ausgabe = "";

        String name = users[aktuellerNutzer];
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;

        double Primogems = (double) js.get("balance");
        double RedditKarma = (double) js.get("RedditKarma");
        double VP_Points = (double) js.get("VpPoints");
        double Toilettenpapier = (double) js.get("Toilettenpapier");
        double Rapsoel = (double) js.get("Rapsoel");
        double NFTs = (double) js.get("NFTs");
        double LSD = (double) js.get("LSD");
        double Grafikkarten = (double) js.get("Grafikkarten");
        double Discord_Nitro = (double) js.get("DiscordNitro");
        double Nieren = (double) js.get("Nieren");
        double CSGO_Case_Keys = (double) js.get("CsgoCaseKeys");

        switch(item) {
            case "Reddit_Karma":
                if(RedditKarma >= amount) {
                    RedditKarma -= amount;
                    Primogems += amount*Reddit_Karma_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viel Reddit_Karma";
                break;

            case "Valorant_Points":
                if(VP_Points >= amount) {
                    VP_Points -= amount;
                    Primogems += amount*Valorant_Points_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viele Valorant_Points";
                break;

            case "Toilettenpapier":
                if(Toilettenpapier >= amount) {
                    Toilettenpapier -= amount;
                    Primogems += amount*Toilettenpapier_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viel Toilettenpapier";
                break;

            case "Rapsoel":
                if(Rapsoel >= amount) {
                    Rapsoel -= amount;
                    Primogems += amount*Rapsoel_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viel Rapsoel";
                break;

            case "NFT":
                if(NFTs >= amount) {
                    NFTs -= amount;
                    Primogems += amount*NFT_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viele NFTs";
                break;

            case "LSD":
                if(LSD >= amount) {
                    LSD -= amount;
                    Primogems += amount*LSD_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viel LSD";
                break;

            case "Grafikkarten":
                if(Grafikkarten >= amount) {
                    Grafikkarten -= amount;
                    Primogems += amount*Grafikkarten_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viele Grafikkarten";
                break;

            case "Discord_Nitro":
                if(Discord_Nitro >= amount) {
                    Discord_Nitro -= amount;
                    Primogems += amount*Discord_Nitro_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viel Discord_Nitro";
                break;

            case "CSGO_Case_Key":
                if(CSGO_Case_Keys >= amount) {
                    CSGO_Case_Keys -= amount;
                    Primogems += amount*CSGO_Case_Key_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viele CSGO_Case_Keys";
                break;

            case "Nieren":
                if(Nieren >= amount) {
                    Nieren -= amount;
                    Primogems += amount*Nieren_Preis;
                    ausgabe = "Verkauf erfolgreich abgeschlossen";
                }else
                    ausgabe = "Sie besitzen nicht so viele Nieren";
                break;

            default:
                ausgabe = "Ungueltige Eingabe, dieses Gut wird nicht angeboten.";
        }

        js.put("balance", Primogems);
        js.put("RedditKarma", RedditKarma);
        js.put("VpPoints", VP_Points);
        js.put("Toilettenpapier", Toilettenpapier);
        js.put("Rapsoel", Rapsoel);
        js.put("NFTs", NFTs);
        js.put("LSD", LSD);
        js.put("Grafikkarten", Grafikkarten);
        js.put("Nieren", Nieren);
        js.put("DiscordNitro", Discord_Nitro);
        js.put("CsgoCaseKeys", CSGO_Case_Keys);

        FileWriter file = new FileWriter(  name + ".json");
        file.write(js.toJSONString());
        file.close();

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

//        Path path = Paths.get("");
//        String directoryName = path.toAbsolutePath().toString() + "\\" + name + ".json";
//        File f = new File(directoryName);
//        f.deleteOnExit();

        ausgabe = "Konto erfolgreich geloescht";

        return ausgabe;
    }


    //bsp: http://localhost:8080/refresh
    /**
     * Aktualisiert die Preise der angebotenen Güter auf Nutzeranfrage.
     * @return einen String, der alle neuen Preise enthält
     */
    @GetMapping("/refresh")
    public String refresh() {
        String ausgabe = "";

        Randomwalk r = new Randomwalk();

        Reddit_Karma_Preis = r.rWalk(Reddit_Karma_Preis);
        Valorant_Points_Preis = r.rWalk(Valorant_Points_Preis);
        Toilettenpapier_Preis = r.rWalk(Toilettenpapier_Preis);
        Rapsoel_Preis = r.rWalk(Rapsoel_Preis);
        NFT_Preis = r.rWalk(NFT_Preis);
        LSD_Preis = r.rWalk(LSD_Preis);
        Grafikkarten_Preis = r.rWalk(Grafikkarten_Preis);
        Discord_Nitro_Preis = r.rWalk(Discord_Nitro_Preis);
        CSGO_Case_Key_Preis = r.rWalk(CSGO_Case_Key_Preis);
        Nieren_Preis = r.rWalk(Nieren_Preis);

        ausgabe = "\n" +
                "Reddit Karma    : " + Reddit_Karma_Preis + " Primogems\n" +
                "Valorant Points : " + Valorant_Points_Preis + " Primogems\n" +
                "Toilettenpapier : " + Toilettenpapier_Preis + " Primogems\n" +
                "Rapsoel         : " + Rapsoel_Preis + " Primogems\n" +
                "NFTs            : " + NFT_Preis + " Primogems\n" +
                "LSD             : " + LSD_Preis + " Primogems\n" +
                "Grafikkarten    : " + Grafikkarten_Preis + " Primogems\n" +
                "Discord Nitro   : " + Discord_Nitro_Preis + " Primogems\n" +
                "CSGO Case Key   : " + CSGO_Case_Key_Preis + " Primogems\n" +
                "Nieren          : " + Nieren_Preis + " Primogems\n";

        return ausgabe;
    }


    //bsp: http://localhost:8080/prices
    /**
     * Gibt die aktuellen Preise der angebotenen Güter auf Nutzeranfrage aus.
     * @return einen String, der alle Preise enthält
     */
    @GetMapping("/prices")
    public String prices() {
        String ausgabe = "";

        ausgabe = "\n" +
                "Reddit Karma    : " + Reddit_Karma_Preis + " Primogems\n" +
                "Valorant Points : " + Valorant_Points_Preis + " Primogems\n" +
                "Toilettenpapier : " + Toilettenpapier_Preis + " Primogems\n" +
                "Rapsoel         : " + Rapsoel_Preis + " Primogems\n" +
                "NFTs            : " + NFT_Preis + " Primogems\n" +
                "LSD             : " + LSD_Preis + " Primogems\n" +
                "Grafikkarten    : " + Grafikkarten_Preis + " Primogems\n" +
                "Discord Nitro   : " + Discord_Nitro_Preis + " Primogems\n" +
                "CSGO Case Key   : " + CSGO_Case_Key_Preis + " Primogems\n" +
                "Nieren          : " + Nieren_Preis + " Primogems\n";


        return ausgabe;
    }


    //bsp: http://localhost:8080/withdraw?amount=10
    /**
     * Zieht die eingebene Menge an Primogems vom Benutzkonto ab
     * @param amount - Menge der Primogems, die abgehoben werden soll
     * @return einen String, der angibt, ob der Abzug erfolgreich war
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/withdraw")
    public String withdraw(@RequestParam(value = "amount") double amount) throws IOException, ParseException {
        String ausgabe = "";

        String name = users[aktuellerNutzer];
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;

        double Primogems = (double) js.get("balance");

        if(Primogems >= amount) {
            Primogems -= amount;
            js.put("balance", Primogems);
            FileWriter file = new FileWriter(  name + ".json");
            file.write(js.toJSONString());
            file.close();
            ausgabe = "Sie haben erfolgreich " + amount + " Primogems abgehoben.";

        } else ausgabe = "So viele Primogems haben Sie nicht auf dem Konto";

        return ausgabe;
    }


    //bsp: http://localhost:8080/deposit?amount=10
    /**
     * Zahlt die eingebene Menge an Primogems in das Benutzkonto ein.
     * @param amount - Menge der Primogems, die eingezahlt werden soll
     * @return einen String, der angibt, ob die Einzahlung erfolgreich war
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("/deposit")
    public String deposit(@RequestParam(value = "amount") double amount) throws IOException, ParseException {
        String name = users[aktuellerNutzer];
        Object o = new JSONParser().parse(new FileReader(name + ".json"));
        JSONObject js = (JSONObject) o;

        double Primogems = (double) js.get("balance");

        Primogems += amount;
        js.put("balance", Primogems);
        FileWriter file = new FileWriter(  name + ".json");
        file.write(js.toJSONString());
        file.close();
        String ausgabe = "Sie haben erfolgreich " +amount+ " Primogems in ihr Konto eingezahlt.";

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