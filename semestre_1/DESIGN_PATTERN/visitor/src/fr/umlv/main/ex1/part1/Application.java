package fr.umlv.main.ex1.part1;

import fr.umlv.main.ex1.com.evilcorp.stp.STPCommand;
import fr.umlv.main.ex1.com.evilcorp.stp.STPParser;

import java.util.Optional;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        var scan = new Scanner(System.in);
        var visitor = new CommandVisitor();
        while(scan.hasNextLine()){
            var line = scan.nextLine();
            if (line.equals("quit")){
                break;
            }
            Optional<STPCommand> answer = STPParser.parse(line);
            if (answer.isEmpty()){
                System.out.println("Unrecognized command");
                continue;
            }
            STPCommand cmd = answer.get();
            cmd.accept(visitor);
        }
    }
}
