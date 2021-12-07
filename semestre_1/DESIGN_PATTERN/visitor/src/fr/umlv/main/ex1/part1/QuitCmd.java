package fr.umlv.main.ex1.part1;

import fr.umlv.main.ex1.com.evilcorp.stp.STPCommand;

public class QuitCmd implements STPCommand {
    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
