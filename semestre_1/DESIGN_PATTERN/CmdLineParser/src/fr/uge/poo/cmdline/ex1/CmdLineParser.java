package fr.uge.poo.cmdline.ex1;

import java.util.*;

public class CmdLineParser {
    private final HashMap<String, Runnable> registeredOptions = new HashMap<>();

    public void addFlag(String option, Runnable code) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        registeredOptions.put(option, code);
    }

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        for (String argument : arguments) {
            if (registeredOptions.containsKey(argument)) {
                registeredOptions.get(argument).run();
            } else {
                files.add(argument);
            }
        }
        return files;
    }
}