package fr.umlv.main.ex1.part1;

import fr.umlv.main.ex1.com.evilcorp.stp.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class CommandVisitor implements  Visitors {
    private final HashMap<Integer,Long> timers = new HashMap<>();
    private final HashSet<ApplicationObserver> observers = new HashSet<>();

    private enum commandType {
        START,
        END,
        UNVALIDEND,
        ELAPSED,
        HELLO
    }

    interface ApplicationObserver {
        default void onStartOfTimer(int timer) {}
        default void onEndOfTimer(int timer, Long timeElapsed) {}
        default void onUnvalidEndOfTimer(int timer) {}
        default void onElapsedTime() {}
        default void onHelloCallback() {}
        default void onFinishedApplication() {}
    }

    static class CounterObserver implements ApplicationObserver {
        private final HashMap<commandType, Integer> counters = new HashMap<>();

        @Override
        public void onStartOfTimer(int timer) {
            if(counters.putIfAbsent(commandType.START, 1) != null) {
                counters.put(commandType.START, counters.get(commandType.START) + 1);
            }
        }

        @Override
        public void onEndOfTimer(int timer, Long timeElapsed) {
            if(counters.putIfAbsent(commandType.END, 1) != null) {
                counters.put(commandType.END, counters.get(commandType.END) + 1);
            }
        }

        @Override
        public void onElapsedTime() {
            if(counters.putIfAbsent(commandType.ELAPSED, 1) != null) {
                counters.put(commandType.ELAPSED, counters.get(commandType.ELAPSED) + 1);
            }
        }

        @Override
        public void onUnvalidEndOfTimer(int timer) {
            if(counters.putIfAbsent(commandType.UNVALIDEND, 1) != null) {
                counters.put(commandType.UNVALIDEND, counters.get(commandType.UNVALIDEND) + 1);
            }
        }

        @Override
        public void onHelloCallback() {
            if(counters.putIfAbsent(commandType.HELLO, 1) != null) {
                counters.put(commandType.HELLO, counters.get(commandType.HELLO) + 1);
            }
        }

        @Override
        public void onFinishedApplication() {
            for(var counter : counters.entrySet()) {
                System.out.println(counter);
            }
        }
    }

    static class AverageObserver implements ApplicationObserver {
        private final HashMap<Integer, ArrayList<Long>> averageTime = new HashMap<>();

        @Override
        public void onEndOfTimer(int timer, Long timeElapsed) {
            averageTime.computeIfAbsent(timer, key -> new ArrayList<>())
                    .add(timeElapsed);
        }

        @Override
        public void onFinishedApplication() {
            for(var key : averageTime.keySet()) {
                var sum = averageTime.get(key).stream().mapToLong(value -> value).sum();
                var average = sum / averageTime.get(key).size();
                System.out.println("Average time for timer '" + key + "' : " + average);
            }
        }
    }

    void registerObserver(ApplicationObserver observer) {
        Objects.requireNonNull(observer);
        observers.add(observer);
    }

    public CommandVisitor() {
        registerObserver(new CounterObserver());
        registerObserver(new AverageObserver());
    }

    public void visit(ElapsedTimeCmd command) {
        var currentTime =  System.currentTimeMillis();
        for(var timerId : command.getTimers()){
            var startTime = timers.get(timerId);
            if (startTime==null){
                System.out.println("Unknown timer "+timerId);
                continue;
            }
            System.out.println("Ellapsed time on timerId "+timerId+" : "+(currentTime-startTime)+"ms");
        }
        observers.forEach(ApplicationObserver::onElapsedTime);
    }

    public void visit(HelloCmd command) {
        System.out.println("Hello the current date is "+ LocalDateTime.now());
        observers.forEach(ApplicationObserver::onHelloCallback);
    }

    public void visit(StartTimerCmd command) {
        var timerId = command.getTimerId();
        if (timers.get(timerId)!=null){
            System.out.println("Timer "+timerId+" was already started");
        }
        var currentTime =  System.currentTimeMillis();
        timers.put(timerId, currentTime);
        observers.forEach(observer -> observer.onStartOfTimer(timerId));
        System.out.println("Timer "+timerId+" started");
    }

    public void visit(StopTimerCmd command) {
        var timerId = command.getTimerId();
        var startTime = timers.get(timerId);
        if (startTime==null){
            System.out.println("Timer "+timerId+" was never started");
            observers.forEach(observer -> observer.onUnvalidEndOfTimer(timerId));
            return;
        }
        var currentTime =  System.currentTimeMillis();
        System.out.println("Timer "+timerId+" was stopped after running for "+(currentTime-startTime)+"ms");
        timers.put(timerId, null);
        observers.forEach(observer -> observer.onEndOfTimer(timerId, currentTime - startTime));

    }

    public void visit(QuitCmd command) {
        observers.forEach(ApplicationObserver::onFinishedApplication);
    }
}
