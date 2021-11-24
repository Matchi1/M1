package fr.uge.poo.cmdline.ex4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CmdLineParser {
    private final HashMap<String, Option> registeredOptions = new HashMap<>();
    private final HashSet<String> mandatoryOptions = new HashSet<>();

    public void addFlag(String option, Runnable code) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.createBuilder(option, 0, arguments -> code.run());
        registeredOptions.put(option, optionBuilder.build());
    }
    
    public void addFlagWithParameter(String option, Consumer<String> code) {
    	Objects.requireNonNull(option);
    	Objects.requireNonNull(code);
    	Consumer<List<String>> action = arguments -> {
    		if(arguments.size() < 1) {
        		throw new IllegalStateOptions("Not enough parameters");
    		}
    		if(Option.isOption(arguments.get(0))) {
    			throw new IllegalStateOptions("This option is waiting for an argument");
    		}
    		code.accept(arguments.get(0));
        };
        var optionBuilder = Option.createBuilder(option, 1, action);
    	registeredOptions.put(option, optionBuilder.build());
    }

    public void addOption(String option, Consumer<List<String>> code, int numberOfParameters, boolean mandatory) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.createBuilder(option, numberOfParameters, code);
        optionBuilder.setMandatory(mandatory);
        if(mandatory) {
            mandatoryOptions.add(option);
        }
        registeredOptions.put(option, optionBuilder.build());
    }
    
    private List<String> getArguments(Iterator<String> arguments, int numberOfParameters){
    	var nextArguments = new ArrayList<String>();
    	for(var i = 0; i < numberOfParameters; i++) {
    		if(arguments.hasNext()) {
    			var argument = arguments.next();
    			if(Option.isOption(argument)) {
    				throw new IllegalStateOptions("Option is waiting for parameters");
    			}
    			nextArguments.add(argument);
    		} else {
    			throw new IllegalStateOptions("Not enough parameters");
    		}
    	}
    	return nextArguments;
    }
    
    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        for(var iterator = List.of(arguments).iterator(); iterator.hasNext();) {
        	var argument = iterator.next();
            var option = registeredOptions.get(argument);
            mandatoryOptions.remove(argument);
            if (option != null) {
            	var nextArguments = getArguments(iterator, option.getNumberOfParameters());
                option.accept(nextArguments);
            } else {
                files.add(argument);
            }
        }
        if(!mandatoryOptions.isEmpty()) {
            throw new IllegalStateOptions("Mandatory options are missing : " + mandatoryOptions.toString());
        }
        return files;
    }
}
