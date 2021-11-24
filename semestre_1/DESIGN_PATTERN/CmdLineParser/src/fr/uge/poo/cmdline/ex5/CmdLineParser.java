package fr.uge.poo.cmdline.ex5;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class CmdLineParser {
    private final HashMap<String, Option> registeredOptions = new HashMap<>();
    private final HashSet<String> mandatoryOptions = new HashSet<>();
    private final HashSet<String> encounteredLabels = new HashSet<>();

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

	public static Option.OptionBuilder oneIntParameter(String optionName, int numberOfParameters, IntConsumer action) {
		return new Option.OptionBuilder(
				optionName,
				numberOfParameters,
				arguments -> action.accept(Integer.parseInt(arguments.get(0))));
	}

	public static Option.OptionBuilder twoIntParameter(String optionName, int numberOfParameters, BiConsumer<Integer, Integer> action) {
		return new Option.OptionBuilder(
				optionName,
				numberOfParameters,
				arguments -> action.accept(Integer.parseInt(arguments.get(0)), Integer.parseInt(arguments.get(1))));
	}

	public static Option.OptionBuilder oneInetSocketParameter(String optionName, int numberOfParameters, Consumer<InetSocketAddress> action) {
		return new Option.OptionBuilder(
				optionName,
				numberOfParameters,
				arguments -> action.accept(new InetSocketAddress(arguments.get(0), Integer.parseInt(arguments.get(1)))));
	}


    public void addOptions(String option, Consumer code, int numberOfParameters,
						   boolean mandatory, List<String> aliases, String description) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        Objects.requireNonNull(aliases);
		Objects.requireNonNull(aliases);
		Objects.requireNonNull(description);
        var optionBuilder = Option.createBuilder(option, numberOfParameters, code);
        optionBuilder.setMandatory(mandatory);
        if(mandatory) {
            mandatoryOptions.add(option);
        }
        aliases.forEach(optionBuilder::addAlias);
		optionBuilder.setDescription(description);
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

    private String getCorrespondingOptionName(String name) {
        for(var option : registeredOptions.values()) {
			if(option.getName().equals(name)) {
				return name;
			}
            if(option.containsAlias(name)) {
				return option.getName();
            }
        }
        return null;
    }

	private void addOptionLabels(String optionName) {
		var option = registeredOptions.get(optionName);
		encounteredLabels.addAll(option.getAliases());
		encounteredLabels.add(optionName);
	}

	private String usage() {
		var usage = new StringBuilder();
		usage.append("java [application]");
		for(var option : registeredOptions.values()) {
			var optionUsage = new StringJoiner(", ", "[", "]");
			optionUsage.add(option.getName());
			option.getAliases().forEach(optionUsage::add);
			usage.append(optionUsage);
		}
		usage.append("\n\n");
		return usage.toString();
	}

	private void help() {
		var helpMessage = new StringBuilder();
		helpMessage.append("Descriptions\n");
		helpMessage.append(usage());
		for(var option : registeredOptions.values()) {
			var emptyLine = true;
			var aliases = new StringJoiner(", ", "", "");
			aliases.add(option.getName());
			option.getAliases().forEach(aliases::add);
			if(!aliases.toString().equals("")) {
				helpMessage.append(aliases);
				helpMessage.append("\t\t");
				emptyLine = false;
			}
			if(!option.getDescription().equals("")) {
				helpMessage.append(option.getDescription());
				emptyLine = false;
			}
			if(!emptyLine) {
				helpMessage.append("\n");
			}
		}
		System.out.println(helpMessage);
	}



	private void addHelpOption() {
		var aliases = List.of(new String[]{"-h"});
		addOptions("--help", arguments -> help(), 0,false, aliases,
				"Show this message");
	}

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
		addHelpOption();
        var files = new ArrayList<String>();
        for(var iterator = List.of(arguments).iterator(); iterator.hasNext();) {
        	var argument = iterator.next();
		 	if(Option.isOption(argument)) {
				var optionName = getCorrespondingOptionName(argument);
				if(optionName != null) {
					mandatoryOptions.remove(optionName);
					addOptionLabels(optionName);
					var option = registeredOptions.get(optionName);
					var nextArguments = getArguments(iterator, option.getNumberOfParameters());
					option.accept(nextArguments);
				}
            } else {
                files.add(argument);
            }
        }
        if(!mandatoryOptions.isEmpty()) {
            throw new IllegalStateOptions("Mandatory options are missing : " + mandatoryOptions);
        }
        return files;
    }
}
