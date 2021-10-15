package fr.uge.poo.ducks;

import java.util.ServiceLoader;

public class DuckFarm {
    public static void main(String[] args) {
        var loader = ServiceLoader.load(fr.uge.poo.ducks.Duck.class);
        for(Duck duck : loader) {
            System.out.println(duck.quack());
        }
    }
}
