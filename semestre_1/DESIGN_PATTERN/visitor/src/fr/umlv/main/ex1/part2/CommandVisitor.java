package fr.umlv.main.ex1.part2;

import fr.umlv.main.ex1.com.evilcorp.stphipster.*;

import java.time.LocalDateTime;
import java.util.HashMap;

public class CommandVisitor implements Visitors {
    private final HashMap<Integer,Long> timers = new HashMap<>();

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
;   }

    public void visit(HelloCmd command) {
        System.out.println("Hello the current date is "+ LocalDateTime.now());
    }

    public void visit(StartTimerCmd command) {
        var timerId = command.getTimerId();
        if (timers.get(timerId)!=null){
            System.out.println("Timer "+timerId+" was already started");
        }
        var currentTime =  System.currentTimeMillis();
        timers.put(timerId,currentTime);
        System.out.println("Timer "+timerId+" started");
    }

    public void visit(StopTimerCmd command) {
        var timerId = command.getTimerId();
        var startTime = timers.get(timerId);
        if (startTime==null){
            System.out.println("Timer "+timerId+" was never started");
        }
        var currentTime =  System.currentTimeMillis();
        System.out.println("Timer "+timerId+" was stopped after running for "+(currentTime-startTime)+"ms");
        timers.put(timerId,null);
    }
}
