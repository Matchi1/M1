package fr.uge.poo.cmdline.ex7;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@FunctionalInterface
public interface ParameterRetrievalStrategy {
    List<String> execute(HashMap<String, Option> options, Iterator<String> iterator, int numberOfParameters);
}
