package fr.umlv.conf;

import java.util.Objects;
import java.util.Optional;

public class LoggerConf {
    private String name;
    private LogLevel level;

    public Optional<Object> name() {
        if(name == null) {
            return Optional.empty();
        }
        return Optional.of(name);
    }

    public Optional<Object> level() {
        if(level == null) {
            return Optional.empty();
        }
        return Optional.of(level);
    }

    public LoggerConf name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }

    public LoggerConf level(LogLevel level) {
        Objects.requireNonNull(level);
        this.level = level;
        return this;
    }

    @Override
    public String toString() {
        return ConfHelper.toString(this, LoggerConf::name, LoggerConf::level);
    }
}
