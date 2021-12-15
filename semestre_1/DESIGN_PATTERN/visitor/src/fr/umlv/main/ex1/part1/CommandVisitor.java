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

    void startTimer(int timer, Long currentTime) {
        timers.put(timer, currentTime);
        notifyStartTimer(timer);
    }

    void endTimer(int timer, Long timeElapsed) {
        timers.put(timer, null);
        notifyEndTimer(timer, timeElapsed);
    }

    void elapsedTime() {
        notifyElapsedTime();
    }

    void helloCallback() {
        notifyHelloCallback();
    }

    void finishApplication() {
        notifyFinishedApplication();
    }

    Long getTimer(int timer) {
        return timers.get(timer);
    }

    void notifyStartTimer(int timer) {
        observers.forEach(observer -> observer.onStartOfTimer(timer));
    }

    void notifyEndTimer(int timer, Long timeElapsed) {
        observers.forEach(observer -> observer.onEndOfTimer(timer, timeElapsed));
    }

    void notifyUnvalidEndTimer(int timer) {
        observers.forEach(observer -> observer.onUnvalidEndOfTimer(timer));
    }

    void notifyElapsedTime() {
        observers.forEach(ApplicationObserver::onElapsedTime);
    }

    void notifyHelloCallback() {
        observers.forEach(ApplicationObserver::onHelloCallback);
    }

    void notifyFinishedApplication() {
        observers.forEach(ApplicationObserver::onFinishedApplication);
    }

    public CommandVisitor() {
        registerObserver(new CounterObserver());
        registerObserver(new AverageObserver());
    }

    public void visit(ElapsedTimeCmd command) {
        var currentTime =  System.currentTimeMillis();
        for(var timerId : command.getTimers()){
            var startTime = getTimer(timerId);
            if (startTime==null){
                System.out.println("Unknown timer "+timerId);
                continue;
            }
            System.out.println("Ellapsed time on timerId "+timerId+" : "+(currentTime-startTime)+"ms");
        }
        elapsedTime();
;   }

    public void visit(HelloCmd command) {
        System.out.println("Hello the current date is "+ LocalDateTime.now());
        helloCallback();
    }

    public void visit(StartTimerCmd command) {
        var timerId = command.getTimerId();
        if (getTimer(timerId)!=null){
            System.out.println("Timer "+timerId+" was already started");
        }
        var currentTime =  System.currentTimeMillis();
        startTimer(timerId, currentTime);
        System.out.println("Timer "+timerId+" started");
    }

    public void visit(StopTimerCmd command) {
        var timerId = command.getTimerId();
        var startTime = getTimer(timerId);
        if (startTime==null){
            System.out.println("Timer "+timerId+" was never started");
            notifyUnvalidEndTimer(timerId);
            return;
        }
        var currentTime =  System.currentTimeMillis();
        System.out.println("Timer "+timerId+" was stopped after running for "+(currentTime-startTime)+"ms");
        endTimer(timerId, currentTime - startTime);
    }

    public void visit(QuitCmd command) {
        finishApplication();
    }
}
