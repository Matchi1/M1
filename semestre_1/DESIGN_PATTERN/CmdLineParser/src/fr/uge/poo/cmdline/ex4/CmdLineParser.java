package fr.uge.poo.cmdline.ex4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CmdLineParser {
    private final HashMap<String, Option> registeredOptions = new HashMap<>();
    private final HashSet<String> mandatoryOptions = new HashSet<>();
    
    private List<String> getListOfArguments(String option, List<String> parameters, int numberOfParameters) {
        var arguments = new ArrayList<String>();
        if(parameters.size() < numberOfParameters) {
        	throw new IllegalStateOptions("Not enough arguments for this option");
        }
        for(var argument : parameters) {
        	if(--numberOfParameters == 0) {
        		break;
        	}
            if(Option.isOption(argument)) {
                throw new IllegalStateOptions("Option '"+ option +"' is waiting for an argument");
            }
            arguments.add(argument);
        }
        return arguments;
    }

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

    public void addFlagWithParameters(String option, Consumer<List<String>> code, int numberOfParameters, boolean mandatory) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        Consumer<List<String>> action = iterator -> {
            var arguments = getListOfArguments(option, iterator, numberOfParameters);
            code.accept(arguments);
        };
        var optionBuilder = Option.createBuilder(option, numberOfParameters, action);
        optionBuilder.setMandatory(mandatory);
        mandatoryOptions.add(option);
        registeredOptions.put(option, optionBuilder.build());
    }

    public void setFlagMandatory(String optionName) {
        mandatoryOptions.add(optionName);
    }

    public List<String> getMandatoryOptions() {
        return registeredOptions.entrySet().stream()
                .filter(entry -> entry.getValue().isMandatory())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    public List<String> getArguments(Iterator<String> arguments, int numberOfParameters){
    	var nextArguments = new ArrayList<String>();
    	for(var i = 0; i < numberOfParameters; i++) {
    		if(arguments.hasNext()) {
    			var argument = arguments.next();
    			if(Option.isOption(argument)) {
    				throw new IllegalStateException();
    			}
    			nextArguments.add(arguments.next());
    		} else {
    			throw new IllegalStateOptions("option error");
    		}
    	}
    	return nextArguments;
    }
    
    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        var iterator = List.of(arguments).iterator();
        for(; iterator.hasNext(); ) {
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