package fr.umlv.main.ex1.com.evilcorp.stphipster;

import java.util.List;
import java.util.Objects;

public final record ElapsedTimeCmd(List<Integer> timers) implements STPCommand {

    public ElapsedTimeCmd {
        Objects.requireNonNull(timers);
    }

    public List<Integer> getTimers() {
        return timers;
    }
}
