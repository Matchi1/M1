package fr.umlv.main.ex1.com.evilcorp.stp;

import fr.umlv.main.ex1.part1.CommandVisitor;

import java.util.List;
import java.util.Objects;

public final class ElapsedTimeCmd implements STPCommand {

    private final List<Integer> timers;

    public ElapsedTimeCmd(List<Integer> timers) {
        Objects.requireNonNull(timers);
        this.timers = List.copyOf(timers);
    }

    public List<Integer> getTimers() {
        return timers;
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
