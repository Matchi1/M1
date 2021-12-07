package fr.umlv.main.ex1.part1;

import fr.umlv.main.ex1.com.evilcorp.stp.*;

public interface Visitors {
    void visit(ElapsedTimeCmd command);
    void visit(HelloCmd command);
    void visit(StartTimerCmd command);
    void visit(StopTimerCmd command);
    void visit(QuitCmd command);
}
