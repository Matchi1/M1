package fr.umlv.ex2;

import fr.uge.poo.logger.q0.SystemLogger;

import java.util.List;

public class LoggerList implements Logger{
    private final List<Logger> list;

    public LoggerList(List<Logger> list) {
        this.list = list;
    }

    @Override
    public void log(SystemLogger.Level level, String message) {
        list.forEach(logger -> {
            logger.log(level, message);
        });
    }
}
