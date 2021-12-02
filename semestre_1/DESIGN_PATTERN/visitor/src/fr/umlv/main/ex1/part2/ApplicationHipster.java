package fr.umlv.main.ex1.part2;

import fr.umlv.main.ex1.com.evilcorp.stphipster.STPCommand;
import fr.umlv.main.ex1.com.evilcorp.stphipster.STPParser;

import java.util.Optional;
import java.util.Scanner;

public class ApplicationHipster {

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
            visitor.visit(cmd);
        }
    }
}
