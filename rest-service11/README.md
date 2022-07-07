Projekt 3: Entwurf eines Multiplayer Spiels in Java mit RESTful Webservice 

Projektziel:
Das Ziel des Projektes ist es einen beliebigen mehrspieler Spiel in Java als RESTful Webservice mit zugehörigem Client zu erstellen.
Dabei soll es möglich sein, mehrere Spiel-Instanzen gleichzeitig auf den Server zu durchlaufen.
Die Nutzer sollen die Möglichkeit haben, ein Spiel zu erstellen oder jemand beizutreten. 
Die Möglichkeit gegen einen Computer zu spielen wird auch angeboten.
Jeder Nutzer des Programms soll über ein Konto verfügen.
Die Nutzer dürfen während der Runde sich ausloggen, dannach treten sie die letzte Runde wieder bei wenn sie sich erneut anmelden. 


Projektumfang:
- RESTful Server implementieren
- Klasse zur Erstellung eines Kontos implementieren
- Klasse zur Erstellung einen Spiel-Instanzen implementieren
- Funktionen, die ausgeführt werden in client implementieren
- Implementation des Ausloggens während einer Runde im Client umsetzen
- RESTful Client implementieren
- Unittests schreiben

Zuletzt sollen alle Klassen mit JavaDoc-Kommentaren versehen werden und das Projekt ausgiebig dokumentiert werden.


Ein UML-Diagram für die Funktionsweise des Servers befindet sich in der Datei "UML-diagramm".

Die Dokumentation für die Build- bzw. Unittests befindet sich in der Datei "Unittests".

Die Dokumentation für die Aufteilung des Projekts und den Erfüllungsgrad befindet sich in der Datei "Projektaufteilung_und_Projektstatus".



Dokumentation der Rest Schnittstelle:
Server:

- @GetMapping("/registeredUsers"):
Gibt einen String aller Namen der registrierten Benutzkontos aus
URI:" http://localhost:8080/registeredUsers"

- @GetMapping("/signup"): 
@RequestParam(value = "name") String name, @RequestParam(value = "password") String password
Benötigt als Eingabe einen Namen und ein Passwort. Erstellt ein neues Bentzkonto.
URI: "http://localhost:8080/signup?name=<name>&password=<password>"

- @GetMapping("/login"): 
@RequestParam(value = "name") String name, @RequestParam(value = "password") String password
Benötigt als Eingabe einen Namen und ein Passwort. Gewährt dem Benutzer bei Erfolg Zugang zum Server.
URI: "http://localhost:8080/login?name=<name>&password=<password>"

- @GetMapping("/createSingleplayerGame"): 
Erstellt ein Singleplayer Game gegen den Computer 
URI: "http://localhost:8080/createSingleplayerGame?name=<name>"

- @GetMapping("/createMultiplayerGame"): 
Erstellt ein Multiplayer Game, dass andere Spieler über den Client beitreten können
URI: "http://localhost:8080/createMultiplayerGame?name=<name>"

- @GetMapping("/join"):
@RequestParam(value = "name") String name
Spieler treten ein existierenden Spiel beim Aufruf, durch konsole Eingabe wird die Name des Spiels bestimmt
URI: "http://localhost:8080/join?name=<name>"

- @GetMapping("/sell"): 
@RequestParam(value = "item") String item, @RequestParam(value = "amount") int amount
Benötigt als Eingabe ein Gut und eine Menge. Verinngert je nachdem die Menge des Gutes innerhalb des Benutzerkontos und erhöht den Kontostand.
URI: "http://localhost:8080/sell?item=<item>&amount=<amount>"

- @GetMapping("/delete"): 
Setzt alle Variablen des angemeldeten Kontos auf den leeren String.
URI: "http://localhost:8080/delete"

- @GetMapping("/refresh"): 
Aktualisiert die Preise und gibt die neuen Preise als String aus.
URI: "http://localhost:8080/refresh"

- @GetMapping("/prices"): 
Gibt die aktuellen Preise als String aus
URI: "http://localhost:8080/prices"

- @GetMapping("/withdraw"): 
@RequestParam(value = "amount") double amount
Benötigt als Eingabe eine Menge. Zieht die Menge vom Kontostand des angemeldeten Benutzerkontos ab, wenn die Menge kleiner gleich dem Kontostand ist.
URI: "http://localhost:8080/withdraw?amount=<amount>"

- @GetMapping("/deposit"):
@RequestParam(value = "amount") double amount
Benötigt als Eingabe eine Menge. Erhöht den Kontostand des angemeldeten Benutzerkontos um die angegebene Menge.
URI: "http://localhost:8080/deposit?amount=<amount>"

- @GetMapping("/shutdown"): 
Schließt den Server.
URI: "http://localhost:8080/shutdown"


Client:
Ruft die vom Server erstellen URIs mittels einer Instanz der Klasse RestTemplate auf.
Dafür wird die Methode getForObject(URI, class) verwendet. Diese führt die GET Methode auf die URI auf und speichert die Rückgabe in einem Element der eingegebenen Klasse.



Verwendete Frameworks: 
Spring: Framkework, der für das Aufsetzen des Servers verantwortlich ist



Verwendete Bibliotheken:
org.json: Gebraucht für die Klasse UserCreation, um JSON Dateinen anzulegen und in der Klasse UserInteraction, um Werte aus den JSON Dateien zu lesen und zu überschreiben

java.io: Gebraucht für die Klasse UserCreation, die Klasse UserInteraction und die Klasse Client, um neue JSON Dateien zu kreieren und Werte aus den JSON Dateien zu überschreiben

java.lang.Math: Die Methode random der Bilbiothek wird für RandomWalk gebraucht

java.nio.file: Gebraucht für die Klasse UserCreation, um JSON Dateinen anzulegen und in das richtige Verzeichnis abzulegen



Autoren: Fengyi Zhao, Maximilian Goertz und Tobias Kupezki
