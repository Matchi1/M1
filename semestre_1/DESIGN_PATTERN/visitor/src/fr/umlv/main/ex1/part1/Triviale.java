package fr.umlv.main.ex1.part1;

import fr.umlv.main.ex1.com.evilcorp.stp.*;

public class Triviale {
    public static void main(String[] args) {
        var command = "start 1";
        var result = STPParser.parse(command);
        if(result.isEmpty()) {
            System.out.println("Pas compris");
        } else if(result.get() instanceof HelloCmd) {
            System.out.println("Au revoir");
        } else {
            System.out.println("Non implémenté");
        }
    }
}
