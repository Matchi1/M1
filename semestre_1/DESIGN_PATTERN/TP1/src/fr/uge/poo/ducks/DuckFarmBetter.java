package fr.uge.poo.ducks;

import java.util.ArrayList;
import java.util.ServiceLoader;

public class DuckFarmBetter {

    public static void main(String[] args) {
        var ducks = new ArrayList<String>();
        var loader = ServiceLoader.load(fr.uge.poo.ducks.DuckFactory.class);
        ducks.add("Fifi");
        ducks.add("Riri");
        ducks.add("Loulou");
        for(DuckFactory duck : loader) {
            for(var name : ducks) {
                System.out.println(duck.withName(name).quack());
            }
        }
    }
}
