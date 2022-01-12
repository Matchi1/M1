package fr.umlv.ex2;

import fr.uge.poo.logger.q0.SystemLogger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Application {
    public static void main(String[] args) throws IOException {
        Predicate<SystemLogger.Level> predicate = level -> !SystemLogger.Level.INFO.equals(level);
        var path = Paths.get("/tmp/", "logs.txt");
        var loggers = new ArrayList<Logger>();
        loggers.add(new LoggerFilter(new PathLogger(path), predicate));
        loggers.add(new LoggerFilter(SystemLogger.getInstance(), predicate));
        var logger = new LoggerList(loggers);
        logger.log(SystemLogger.Level.ERROR, "urgennnnnnnnnt ça s'affiche ?");
        logger.log(SystemLogger.Level.INFO, "info: ne s'affiche pas");
        logger.log(SystemLogger.Level.WARNING, "un petit warning qui s'affiche, j'espère");
    }
}
