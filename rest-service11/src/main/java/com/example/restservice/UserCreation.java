package com.example.restservice;
import org.json.simple.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Klasse zur Erstellung eines neuen Benutzers.
 */
public class UserCreation {

   public UserCreation(){}

     /**
      * Erstellt eine .json Datei mit den übergebenen Parametern name und password und vordefinierten
      * Parametern balance und die Menge der 10 Güter.
      *
      * @param name für Parameter name innerhalb der .json und dem Namen der Datei für den FileWriter
      * @param password für Parameter password innerhalb der .json
      * @throws IOException bei Problemen mit FileWriter
      */

        public void newUser(String name, String password) throws IOException{
             Path path = Paths.get("");
             JSONObject newUser = new JSONObject();

             newUser.put("name", name);
             newUser.put("password", password);
             newUser.put("hasGame", false);

             FileWriter file = new FileWriter(  name + ".json");
             file.write(newUser.toJSONString());
             file.close();
    }
}