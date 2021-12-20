package fr.umlv.ex2;

import fr.uge.poo.logger.q0.SystemLogger;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Application {
    public static void main(String[] args) {
        Predicate<SystemLogger.Level> predicate = level -> !SystemLogger.Level.INFO.equals(level);
        var logger = new LoggerList();
    }
}
