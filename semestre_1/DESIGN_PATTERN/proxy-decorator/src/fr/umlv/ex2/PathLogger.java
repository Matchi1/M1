package fr.umlv.ex2;

import fr.uge.poo.logger.q0.SystemLogger;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class PathLogger implements Closeable, Logger {
    private boolean closed = false;
    private final BufferedWriter writer;

    public PathLogger(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    @Override
    public void log(SystemLogger.Level level, String message) {
        if(closed) {
            return;
        }
        try {
            writer.write(level + " " + message + "\n");
            writer.flush();
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
        writer.close();
    }
}
