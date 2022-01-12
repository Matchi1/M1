package fr.umlv.conf;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfHelper  {
    public static <E> String toString(LoggerConf logger, Optional<E>... getters) {
        Objects.requireNonNull(logger);

        return Arrays.stream(getters)
                .filter(Optional::isPresent)
                .map(getter -> getter.get().toString())
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
