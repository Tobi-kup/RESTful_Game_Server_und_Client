package com.example.restservice;
import static java.lang.Math.*;

public class Randomwalk {

    /**
     * Initialisiere Werte
     * kurs = startwert
     * tendenz = tendenz des handelwerts
     * dt = zeitschritt
     * streuung = standartabweichung
     * y = variable für randomnumber
     */
    double kurs = 5.0*(2* Math.random() - 1.0);
    double tendenz = 0.3*(2* Math.random() - 1.0);
    double dt = 0.001;
    double streuung = 0.8;
    double y = 0;

    /**
     * nimmt eingabe als startwert ein und generiert entsprechend den nächsten randomwalk wert
     * tendez und streuung einstellbar durch werte hier
     * ist immer random ;)
     * @param eingabe ist eingangswert
     * @return eingabe ist eingangswert modifiziert durch random
     */
    public double rWalk(double eingabe){
        double sqdt = sqrt(dt);
        double y = 2* Math.random() - 1.0;
        //kurs = kurs * (1 + tendenz * dt + streuung * sqdt * y);
        eingabe = eingabe * (1 + tendenz * dt + streuung * sqdt * y);
        //return kurs;
        return eingabe;
    }

    /**
     * testklasse dass randomwalk 1000 mal ausführt und ausgibt
     */
    public void walkGenerate(){
        for(int i=0; i<1000; i++){
            System.out.println(rWalk(kurs));
        }
    }
}
