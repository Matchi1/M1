package fr.uge.poo.cmdline.ex7;

import java.util.Iterator;
import java.util.List;

@FunctionalInterface
public interface ParameterRetrievalStrategy {
    List<String> execute(Iterator<String> iterator, int numberOfParameters);
}
