package fr.umlv.main.ex1.part1;

import fr.umlv.main.ex1.com.evilcorp.stp.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class CommandVisitor implements  Visitors {
    private final ApplicationManager manager = new ApplicationManager();

    interface ApplicationObserver {
        default void onStartOfTimer(ApplicationManager manager, int timer, Long startTime) {}
        default void onEndOfTimer(ApplicationManager manager, int timer) {}
        default void onElapsedTime(ApplicationManager manager) {}
        default void onHelloCallback(ApplicationManager manager) {}
        default void onFinishedApplication(ApplicationManager manager) {}
    }

    static class CounterObserver implements ApplicationObserver {
        private final HashMap<String, Integer> counters = new HashMap<>();

        @Override
        public void onStartOfTimer(ApplicationManager manager, int timer, Long startTime) {
            if(counters.putIfAbsent("start", 1) != null) {
                counters.put("start", counters.get("start") + 1);
            }
        }

        @Override
        public void onEndOfTimer(ApplicationManager manager, int timer) {
            if(counters.putIfAbsent("end", 1) != null) {
                counters.put("end", counters.get("end") + 1);
            }
        }

        @Override
        public void onElapsedTime(ApplicationManager manager) {
            if(counters.putIfAbsent("elapsed", 1) != null) {
                counters.put("elapsed", counters.get("elapsed") + 1);
            }
        }

        @Override
        public void onHelloCallback(ApplicationManager manager) {
            if(counters.putIfAbsent("hello", 1) != null) {
                counters.put("hello", counters.get("hello") + 1);
            }
        }

        @Override
        public void onFinishedApplication(ApplicationManager manager) {
            for(var counter : counters.entrySet()) {
                System.out.println(counter);
            }
        }
    }

    static class AverageObserver implements ApplicationObserver {
        private final HashMap<Integer, ArrayList<Long>> timeElapsed = new HashMap<>();
        private final HashMap<Integer, Long> start = new HashMap<>();

        @Override
        public void onStartOfTimer(ApplicationManager manager, int timer, Long startTime) {
            start.put(timer, startTime);
        }

        @Override
        public void onEndOfTimer(ApplicationManager manager, int timer) {
            timeElapsed.computeIfAbsent(timer, key -> new ArrayList<>())
                    .add(System.currentTimeMillis() - start.get(timer));
        }

        @Override
        public void onFinishedApplication(ApplicationManager manager) {
            for(var key : start.keySet()) {
                var sum = timeElapsed.get(key).stream().mapToLong(value -> value).sum();
                System.out.println("Average time for timer '" + key + "' : " + sum);
            }
        }
    }

    public static class ApplicationManager {
        private final HashMap<Integer,Long> timers = new HashMap<>();
        private final HashSet<ApplicationObserver> observers = new HashSet<>();

        void registerObserver(ApplicationObserver observer) {
            Objects.requireNonNull(observer);
            observers.add(observer);
        }

        void startTimer(int timer, Long currentTime) {
            timers.put(timer, currentTime);
            notifyStartTimer(timer, currentTime);
        }

        void endTimer(int timer) {
            timers.put(timer, null);
            notifyEndTimer(timer);
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

        void notifyStartTimer(int timer, Long timeStart) {
            observers.forEach(observer -> observer.onStartOfTimer(this, timer, timeStart));
        }

        void notifyEndTimer(int timer) {
            observers.forEach(observer -> observer.onEndOfTimer(this, timer));
        }

        void notifyElapsedTime() {
            observers.forEach(observer -> observer.onElapsedTime(this));
        }

        void notifyHelloCallback() {
            observers.forEach(observer -> observer.onHelloCallback(this));
        }

        void notifyFinishedApplication() {
            observers.forEach(observer -> observer.onFinishedApplication(this));
        }
    }

    public CommandVisitor() {
        manager.registerObserver(new CounterObserver());
        manager.registerObserver(new AverageObserver());
    }

    public void visit(ElapsedTimeCmd command) {
        var currentTime =  System.currentTimeMillis();
        for(var timerId : command.getTimers()){
            var startTime = manager.getTimer(timerId);
            if (startTime==null){
                System.out.println("Unknown timer "+timerId);
                continue;
            }
            System.out.println("Ellapsed time on timerId "+timerId+" : "+(currentTime-startTime)+"ms");
        }
        manager.elapsedTime();
;   }

    public void visit(HelloCmd command) {
        System.out.println("Hello the current date is "+ LocalDateTime.now());
        manager.helloCallback();
    }

    public void visit(StartTimerCmd command) {
        var timerId = command.getTimerId();
        if (manager.getTimer(timerId)!=null){
            System.out.println("Timer "+timerId+" was already started");
        }
        var currentTime =  System.currentTimeMillis();
        manager.startTimer(timerId, currentTime);
        System.out.println("Timer "+timerId+" started");
    }

    public void visit(StopTimerCmd command) {
        var timerId = command.getTimerId();
        var startTime = manager.getTimer(timerId);
        if (startTime==null){
            System.out.println("Timer "+timerId+" was never started");
        }
        var currentTime =  System.currentTimeMillis();
        System.out.println("Timer "+timerId+" was stopped after running for "+(currentTime-startTime)+"ms");
        manager.endTimer(timerId);
    }

    public void visit(QuitCmd command) {
        manager.finishApplication();
    }
}
