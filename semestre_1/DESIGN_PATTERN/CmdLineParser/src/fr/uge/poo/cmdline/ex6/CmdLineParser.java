package fr.uge.poo.cmdline.ex6;

import java.util.*;
import java.util.function.Consumer;

public class CmdLineParser {
    private final OptionsManager manager = new OptionsManager();
    private final UsageObserver usageObserver = new UsageObserver();

    CmdLineParser() {
        manager.registerObserver(new ConflictObserver());
        manager.registerObserver(new MandatoryOptionObserver());
    }

    interface OptionsManagerObserver {
        default void onRegisteredOption(OptionsManager optionsManager, Option option) {}

        default void onProcessedOption(OptionsManager optionsManager, Option option) {}

        default void onFinishedProcess(OptionsManager optionsManager) {}
    }

    private static class MandatoryOptionObserver implements OptionsManagerObserver {
        private final HashSet<String> mandatoryOptions = new HashSet<>();
        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
            if(option.isMandatory()) {
                mandatoryOptions.add(option.getName());
            }
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
            mandatoryOptions.remove(option.getName());
        }

        @Override
        public void onFinishedProcess(OptionsManager optionsManager) {
            if(!mandatoryOptions.isEmpty()) {
                throw new ParseException("Following mandatory options are still not called : " + mandatoryOptions);
            }
        }
    }

    private static class UsageObserver implements OptionsManagerObserver {
        public String usage(OptionsManager optionsManager) {
            var options = new HashSet<>(optionsManager.byName.values());
            var usage = new StringBuilder();
            usage.append("java [application]");
            options.forEach(option -> {
                var optionUsage = new StringJoiner(", ", "[", "]");
                optionUsage.add(option.getName());
                option.getAliases().forEach(optionUsage::add);
                usage.append(optionUsage);
            });
            usage.append("\n\n");
            options.forEach(option -> usage.append(option.toString()));
            return usage.toString();
        }
    }

    private static class ConflictObserver implements OptionsManagerObserver {
        private final HashSet<String> encounteredOptions = new HashSet<>();

        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
            encounteredOptions.add(option.getName());
            encounteredOptions.addAll(option.getAliases());
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
            var conflicts = option.getConflicts();
            conflicts.forEach(name -> {
                if(encounteredOptions.contains(name)) {
                    throw new ParseException("Options conflict between" + option.getName() + " and " + name);
                }
            });
        }
    }
    
    private static class OptionsManager {
        private final HashMap<String, Option> byName = new HashMap<>();
        private final HashSet<OptionsManagerObserver> observers = new HashSet<>();

        /**
         * Register the option with all its possible names
         *
         * @param option Option object
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
         * @param optionName name of the option
         * @return the corresponding object option if it exists
         */

        Optional<Option> processOption(String optionName) {
            var option = Optional.ofNullable(byName.get(optionName));
            option.ifPresent(this::notifyAllObserverProcessedOption);
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
        		throw new ParseException("Not enough parameters");
    		}
    		if(Option.isOption(arguments.get(0))) {
    			throw new ParseException("This option is waiting for an argument");
    		}
    		code.accept(arguments.get(0));
        };
        var optionBuilder = Option.createBuilder(option, 1, action);
    	manager.register(optionBuilder.build());
    }

    public void addOption(Option option) {
        Objects.requireNonNull(option);
        manager.register(option);
    }
    
    private List<String> getArguments(Iterator<String> arguments, int numberOfParameters){
    	var nextArguments = new ArrayList<String>();
    	for(var i = 0; i < numberOfParameters; i++) {
    		if(arguments.hasNext()) {
    			var argument = arguments.next();
    			if(Option.isOption(argument)) {
    				throw new ParseException("Option is waiting for parameters");
    			}
    			nextArguments.add(argument);
    		} else {
    			throw new ParseException("Not enough parameters");
    		}
    	}
    	return nextArguments;
    }

    private String usage() {
        return usageObserver.usage(manager);
    }

    public List<String> process(String[] arguments) {
        Objects.requireNonNull(arguments);
        var files = new ArrayList<String>();
        for(var iterator = List.of(arguments).iterator(); iterator.hasNext();) {
        	var argument = iterator.next();
		 	if(Option.isOption(argument)) {
                 var option = manager.byName.get(argument);
                 option.accept(getArguments(iterator, option.getNumberOfParameters()));
                 manager.processOption(argument);
            } else {
                files.add(argument);
            }
        }
        manager.finishProcess();
        return files;
    }
}
