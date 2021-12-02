package fr.umlv.main.ex1.com.evilcorp.stp;

import fr.umlv.main.ex1.part1.CommandVisitor;

public final class HelloCmd implements STPCommand {
    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
