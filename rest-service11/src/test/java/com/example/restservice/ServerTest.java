package com.example.restservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;


public class ServerTest {

    RestTemplate rt = new RestTemplate();


    @Test
    @DisplayName("Login ohne vorherige Registrierung")
    void loginOhneRegistrierung(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);

        if(eingabe.equals("Es ist noch kein Nutzer registriert.") || eingabe.equals("Dieser Nutzer existiert nicht."))System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Regisrierung eines ersten Benutzers")
    void registriereErstenBenutzer(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Max&password=123", String.class);
        if(eingabe.equals("Erfolgreich registiert.")) System.out.println("True");
        else System.out.println("False");

//            String inputString = "Hello\n";
//            Scanner input = new Scanner(new InputStreamReader(
//                    new ByteArrayInputStream(inputString.getBytes())));
//            assertEquals("Hello", SocketIO.readFromInputStream(input));
    }

    @Test
    @DisplayName("Regisrierung eines bereits existenten Benutzers")
    void regisriereBereitsExistentenBenutzer(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Max&password=123", String.class);

        if(eingabe.equals("User existiert bereits.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Regisrierung eines neuen Benutzers, wenn das Benutzerarray schon voll ist")
    void regisriereNeuenBenutzerWennArrayVoll(){
        String eingabe = rt.getForObject("http://localhost:8080/signup?name=Max&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=Fengyi&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=Tobi&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=4&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=5&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=6&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=7&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=8&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=9&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=10&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/signup?name=11&password=123", String.class);

        if(eingabe.equals("Die maximale Anzahl an registrierten Nutzern ist bereits erreicht.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Erfolgreicher Login")
    void loginErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);

        if(eingabe.equals("Login erfolgreich.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Login mit falschem Passwort")
    void loginMitFalschemPasswort(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=12345", String.class);

        if(eingabe.equals("Passwort ist falsch.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Login mit falschem Namen")
    void loginMitFalschemNamen(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Maximilian&password=123", String.class);

        if(eingabe.equals("Dieser Nutzer existiert nicht.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("1 Gut kaufen, das nicht angeboten wird")
    void buyNichtExistent(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
         eingabe = rt.getForObject("http://localhost:8080/buy?item=Youtube_Premium&amount=1", String.class);

        if(eingabe.equals("Ungueltige Eingabe, dieses Gut wird nicht angeboten.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("1 Gut kaufen, das angeboten wird mit genug Geld")
    void buyErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/buy?item=Reddit_Karma&amount=1", String.class);

        if(eingabe.equals("Kauf erfolgreich abgeschlossen")) System.out.println("True");
        else System.out.println("False");

    }


    @Test
    @DisplayName("1 Gut kaufen, das angeboten wird mit zu wenig Geld")
    void buyZuWenigGeld(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/buy?item=NFT&amount=1", String.class);

        if(eingabe.equals("Sie haben nicht genug Primogems.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("1 Gut verkaufen, das nicht angeboten wird")
    void sellNichtExistent(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/sell?item=Youtube_Premium&amount=1", String.class);

        if(eingabe.equals("Ungueltige Eingabe, dieses Gut wird nicht angeboten.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("1 Gut verkaufen, das angeboten wird mit 1 im Inventar")
    void sellErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/buy?item=LSD&amount=1", String.class);
        eingabe = rt.getForObject("http://localhost:8080/sell?item=LSD&amount=1", String.class);

        if(eingabe.equals("Verkauf erfolgreich abgeschlossen")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("1 Gut verkaufen, das angeboten wird mit 0 im Inventar")
    void sellNichtGenugGueter(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/sell?item=NFT&amount=1", String.class);

        if(eingabe.equals("Sie besitzen nicht so viele NFTs")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'info'")
    void infoErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/info", String.class);

        if(!eingabe.equals(null)) System.out.println("True");
        else System.out.println("False");

    }


    @Test
    @DisplayName("Verwendung von 'refresh'")
    void refreshErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/refresh", String.class);

        if(!eingabe.equals(null)) System.out.println("True");
        else System.out.println("False");

    }


    @Test
    @DisplayName("Verwendung von 'prices'")
    void pricesErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/prices", String.class);

        if(!eingabe.equals(null)) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'withdraw' mit genug auf dem Konto")
    void withdrawErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/withdraw?amount=10", String.class);

        if(eingabe.equals("Sie haben erfolgreich 10.0 Primogems abgehoben.")) System.out.println("True");
        else System.out.println("False");
    }

    @Test
    @DisplayName("Verwendung von 'withdraw' mit groesserer Angabe als Geld auf dem Konto")
    void withdrawZuViel(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/withdraw?amount=1000000", String.class);

        if(eingabe.equals("So viele Primogems haben Sie nicht auf dem Konto")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'deposit'")
    void depositErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/deposit?amount=10", String.class);

        if(eingabe.equals("Sie haben erfolgreich 10.0 Primogems in ihr Konto eingezahlt.")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'delete' nach Anmeldung")
    void deleteErfolgreich(){
        String eingabe = rt.getForObject("http://localhost:8080/login?name=Max&password=123", String.class);
        eingabe = rt.getForObject("http://localhost:8080/delete", String.class);

        if(eingabe.equals("Konto erfolgreich geloescht")) System.out.println("True");
        else System.out.println("False");
    }


    @Test
    @DisplayName("Verwendung von 'delete' ohne Anmeldung")
    void deleteFehler(){
        String eingabe = rt.getForObject("http://localhost:8080/delete", String.class);

        if(eingabe.equals("Sie müssen angemeldet sein, um ihr Konto zu löschen.")) System.out.println("True");
        else System.out.println("False");
    }

}
