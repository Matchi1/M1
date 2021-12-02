package fr.umlv.main.ex1.com.evilcorp.stp;

import fr.umlv.main.ex1.part1.CommandVisitor;

public final class StopTimerCmd implements STPCommand {

    private int  timerId;

    public StopTimerCmd(int timerId){
        this.timerId=timerId;
    }

    public int getTimerId() {
        return timerId;
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
