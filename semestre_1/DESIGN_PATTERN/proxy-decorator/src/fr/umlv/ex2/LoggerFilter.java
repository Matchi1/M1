package fr.umlv.ex2;

import fr.uge.poo.logger.q0.SystemLogger;
import java.util.Objects;
import java.util.function.Predicate;

public class LoggerFilter implements Logger {
    private final Logger logger;
    private final Predicate<SystemLogger.Level> predicate;

    public LoggerFilter(Logger logger, Predicate<SystemLogger.Level> predicate) {
        this.predicate = Objects.requireNonNull(predicate);
        this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public void log(SystemLogger.Level level, String message) {
        if(predicate.test(level)) {
            logger.log(level, message);
        }
    }
}
