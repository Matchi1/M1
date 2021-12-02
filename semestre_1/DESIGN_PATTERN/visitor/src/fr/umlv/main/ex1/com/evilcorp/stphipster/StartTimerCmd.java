package fr.umlv.main.ex1.com.evilcorp.stphipster;

public record StartTimerCmd(int timerId) implements STPCommand {

    public StartTimerCmd(int timerId){
        this.timerId=timerId;
    }

    public int getTimerId() {
        return timerId;
    }
}
