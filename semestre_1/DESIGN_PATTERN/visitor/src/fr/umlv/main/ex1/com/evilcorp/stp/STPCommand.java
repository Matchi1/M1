package fr.umlv.main.ex1.com.evilcorp.stp;

import fr.umlv.main.ex1.part1.CommandVisitor;

public interface STPCommand {
    void accept(CommandVisitor visitor);
}
