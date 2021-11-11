package fr.uge.poo.cmdline.ex4;

import java.util.*;
import java.util.function.Consumer;

public class CmdLineParser {
    private final HashMap<String, Option> registeredOptions = new HashMap<>();

    public void addFlag(String option, Runnable code) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.builder();
        optionBuilder.setOption(option);
        optionBuilder.setCode(arguments -> code.run());
        optionBuilder.setNumberOfParameters(0);
        registeredOptions.put(option, optionBuilder.build());
    }
    
    public void addFlagWithParameter(String option, Consumer<String> code) {
    	Objects.requireNonNull(option);
    	Objects.requireNonNull(code);
        var optionBuilder = Option.builder();
        optionBuilder.setOption(option);
        optionBuilder.setCode(iterator -> {
            if(iterator.hasNext()) {
                code.accept(iterator.next());
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        });
        optionBuilder.setNumberOfParameters(1);
    	registeredOptions.put(option, optionBuilder.build());
    }

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        var iterator = List.of(arguments).iterator();
        while(iterator.hasNext()) {
            var argument = iterator.next();
            if (registeredOptions.get(argument) != null) {
                registeredOptions.get(argument).accept(iterator);
            } else {
                files.add(argument);
            }
        }
        return files;
    }
}