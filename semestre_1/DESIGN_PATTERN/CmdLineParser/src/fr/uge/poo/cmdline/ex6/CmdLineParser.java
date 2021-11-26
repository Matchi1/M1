package fr.uge.poo.cmdline.ex6;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class CmdLineParser {
    private final OptionsManager manager = new OptionsManager();
    private final HashSet<String> mandatoryOptions = new HashSet<>();
    private final HashSet<String> encounteredLabels = new HashSet<>();
    
    interface OptionsManagerObserver {
        default void onRegisteredOption(OptionsManager optionsManager,Option option) {};

        default void onProcessedOption(OptionsManager optionsManager,Option option) {};

        default void onFinishedProcess(OptionsManager optionsManager) {};
    }
    
    class LoggerObserver implements OptionsManagerObserver {

        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
            System.out.println("Option "+option+ " is registered");
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
            System.out.println("Option "+option+ " is processed");
        }

        @Override
        public void onFinishedProcess(OptionsManager optionsManager) {
            System.out.println("Process method is finished");
        }
    }
    
    private static class OptionsManager {

        private final HashMap<String, Option> byName = new HashMap<>();
        private final HashSet<OptionsManagerObserver> observers = new HashSet<>();

        /**
         * Register the option with all its possible names
         * @param option
         */
        void register(Option option) {
            register(option.getName(), option);
            for (var alias : option.getAliases()) {
                register(alias, option);
            }
            notifyAllObserverRegisterOption(option);
        }

        private void register(String name, Option option) {
            if (byName.containsKey(name)) {
                throw new IllegalStateException("Option " + name + " is already registered.");
            }
            byName.put(name, option);
        }

        /**
         * This method is called to signal that an option is encountered during 
         * a command line process
         *
         * @param optionName
         * @return the corresponding object option if it exists
         */

        Optional<Option> processOption(String optionName) {
        	var option = Optional.ofNullable(byName.get(optionName));
        	if(option.isPresent()) {
        		notifyAllObserverProcessedOption(option.get());
        	}
            return option;
        }

        /**
         * This method is called to signal the method process of the CmdLineParser is finished
         */
        void finishProcess() {
        	notifyAllObservedFinishedProcess();
        }
        
        void registerObserver(OptionsManagerObserver observer) {
        	Objects.requireNonNull(observer);
        	this.observers.add(observer);
        }
        
        private void notifyAllObserverRegisterOption(Option option) {
        	observers.forEach(observer -> observer.onRegisteredOption(this, option));
        }
        
        private void notifyAllObserverProcessedOption(Option option) {
        	observers.forEach(observer -> observer.onProcessedOption(this, option));
        }
        
        private void notifyAllObservedFinishedProcess() {
        	observers.forEach(observer -> observer.onFinishedProcess(this));
        }
    }

    public void addFlag(String option, Runnable code) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.createBuilder(option, 0, arguments -> code.run());
        manager.register(optionBuilder.build());
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
    	manager.register(optionBuilder.build());    	
    }

    public void addOption(String option, Consumer<List<String>> code, int numberOfParameters, boolean mandatory) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        var optionBuilder = Option.createBuilder(option, numberOfParameters, code);
        optionBuilder.setMandatory(mandatory);
        if(mandatory) {
            mandatoryOptions.add(option);
        }
        manager.register(optionBuilder.build());
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


    public void addOptions(String option, Consumer<List<String>> code, int numberOfParameters,
						   boolean mandatory, List<String> aliases, String description) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(code);
        Objects.requireNonNull(aliases);
		Objects.requireNonNull(description);
        var optionBuilder = Option.createBuilder(option, numberOfParameters, code);
        optionBuilder.setMandatory(mandatory);
        if(mandatory) {
            mandatoryOptions.add(option);
        }
        aliases.forEach(optionBuilder::addAlias);
		optionBuilder.setDescription(description);
        manager.register(optionBuilder.build());
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

    /*
	private void addHelpOption() {
		var aliases = List.of(new String[]{"-h"});
		addOptions("--help", arguments -> help(), 0,false, aliases,
				"Show this message");
	}
	*/
    
    public void registerObserver(OptionsManagerObserver observer) {
    	manager.registerObserver(observer);
    }

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        for(var iterator = List.of(arguments).iterator(); iterator.hasNext();) {
        	var argument = iterator.next();
		 	if(Option.isOption(argument)) {
            } else {
                files.add(argument);
            }
        }
        if(!mandatoryOptions.isEmpty()) {
            throw new IllegalStateOptions("Mandatory options are missing : " + mandatoryOptions);
        }
        manager.finishProcess();
        return files;
    }
}
