package fr.umlv.main.ex1.com.evilcorp.stphipster;

public record StopTimerCmd(int timerId) implements STPCommand {

    public StopTimerCmd(int timerId){
        this.timerId=timerId;
    }

    public int getTimerId() {
        return timerId;
    }

}
