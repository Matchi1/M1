package fr.uge.poo.cmdline.ex4;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CmdLineParser {
    private final HashMap<String, Option.OptionBuilder> registeredOptions = new HashMap<>();

    private List<String> getListOfArguments(String option, Iterator<String> iterator, int numberOfParameters) {
        var arguments = new ArrayList<String>();
        for(var i = 0; i < numberOfParameters; i++) {
            if(iterator.hasNext()) {
                var argument = iterator.next();
                if(Option.isOption(argument)) {
                    throw new IllegalStateOptions("Option '"+ option +"' is waiting for an argument");
                }
                arguments.add(argument);
            } else {
                throw new IllegalStateOptions("Missing argument for the following option : " + option);
            }
        }
        return arguments;
    }

    public void addFlag(String option, Runnable code) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.builder();
        optionBuilder.setOption(option);
        optionBuilder.setCode(arguments -> code.run());
        optionBuilder.setNumberOfParameters(0);
        registeredOptions.put(option, optionBuilder);
    }
    
    public void addFlagWithParameter(String option, Consumer<String> code) {
    	Objects.requireNonNull(option);
    	Objects.requireNonNull(code);
        var optionBuilder = Option.builder();
        optionBuilder.setOption(option);
        optionBuilder.setCode(iterator -> {
            var arguments = getListOfArguments(option, iterator, 1);
            code.accept(arguments.get(0));
        });
        optionBuilder.setNumberOfParameters(1);
    	registeredOptions.put(option, optionBuilder);
    }

    public void addFlagWithParameters(String option, Consumer<List<String>> code, int numberOfParameters) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.builder();
        optionBuilder.setOption(option);
        optionBuilder.setCode(iterator -> {
            var arguments = getListOfArguments(option, iterator, numberOfParameters);
            code.accept(arguments);
        });
        optionBuilder.setNumberOfParameters(numberOfParameters);
        registeredOptions.put(option, optionBuilder);
    }

    public void setFlagMandatory(String optionName) {
        var option = registeredOptions.get(optionName);
        if(option != null){
            option.setMandatory(true);
        }
    }

    public List<String> getMandatoryOptions() {
        return registeredOptions.entrySet().stream()
                .filter(entry -> entry.getValue().build().isMandatory())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        var mandatoryOptions = getMandatoryOptions();
        for(var iterator = List.of(arguments).iterator(); iterator.hasNext();) {
            var argument = iterator.next();
            var option = registeredOptions.get(argument);
            mandatoryOptions.remove(argument);
            if (option != null) {
                option.build().accept(iterator);
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