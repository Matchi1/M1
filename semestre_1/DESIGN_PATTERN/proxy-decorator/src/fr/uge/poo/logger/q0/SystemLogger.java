package fr.uge.poo.logger.q0;

import fr.umlv.ex2.Logger;

public class SystemLogger implements Logger {
    public enum Level {
        ERROR, WARNING, INFO
    }

    private SystemLogger() {}

    private static final SystemLogger INSTANCE = new SystemLogger();

    public static SystemLogger getInstance() {
        return INSTANCE;
    }

    public void log(Level level, String message) {
        System.err.println(level + " " + message);
    }
}