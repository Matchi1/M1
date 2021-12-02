package fr.umlv.main.ex1.part2;

import fr.umlv.main.ex1.com.evilcorp.stphipster.*;

public interface Visitors {
    void visit(ElapsedTimeCmd command);
    void visit(HelloCmd command);
    void visit(StartTimerCmd command);
    void visit(StopTimerCmd command);
    default void visit(STPCommand command) {
        switch (command) {
            case ElapsedTimeCmd elapsed -> visit(elapsed);
            case HelloCmd hello -> visit(hello);
            case StartTimerCmd start -> visit(start);
            case StopTimerCmd stop -> visit(stop);
        }
    }
}
