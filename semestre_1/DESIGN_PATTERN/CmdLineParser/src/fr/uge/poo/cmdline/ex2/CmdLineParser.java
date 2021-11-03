package fr.uge.poo.cmdline.ex2;

import java.util.*;
import java.util.function.Consumer;

public class CmdLineParser {
    private final HashMap<String, Consumer<Iterator<String>>> registeredOptions = new HashMap<>();

    public void addFlag(String option, Runnable code) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        registeredOptions.put(option, iterator -> code.run());
    }
    
    public void addFlagWithParameter(String option, Consumer<String> code) {
    	Objects.requireNonNull(option);
    	Objects.requireNonNull(code);
    	registeredOptions.put(option, iterator -> {
    		if(iterator.hasNext()) {
    			code.accept(iterator.next());
    		} else {
    			throw new ArrayIndexOutOfBoundsException();
    		}
    	});
    }

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        var iterator = List.of(arguments).iterator();
        while(iterator.hasNext()) {
            var argument = iterator.next();
            if (registeredOptions.containsKey(argument)) {
                registeredOptions.get(argument).accept(iterator);
            } else {
                files.add(argument);
            }
        }
        return files;
    }
}