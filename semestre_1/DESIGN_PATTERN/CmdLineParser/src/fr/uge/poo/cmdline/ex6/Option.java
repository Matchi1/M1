package fr.uge.poo.cmdline.ex6;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Option {
    private final String name;
    private final int numberOfParameters;
    private final boolean mandatory;
    private final Consumer<List<String>> action;
    private final HashSet<String> aliases;
    private final HashSet<String> conflicts;
    private final String description;

    public static class OptionBuilder {
        private String name = "";
        private int numberOfParameters = -1;
        private boolean mandatory = false;
        private Consumer<List<String>> action = null;
        private HashSet<String> aliases = new HashSet<>();
        private HashSet<String> conflicts = new HashSet<>();
		private String description = "";

        public OptionBuilder(String name, int numberOfParameters, Consumer<List<String>> action) {
			this.name = name;
			this.numberOfParameters = numberOfParameters;
			this.action = action;
		}

        public OptionBuilder() {
        }

        public OptionBuilder setName(String option) {
            this.name = option;
            return this;
        }

        public OptionBuilder setCode(Consumer<List<String>> action) {
            this.action = action;
            return this;
        }

        public OptionBuilder required() {
            this.mandatory = true;
            return this;
        }

        public OptionBuilder setNumberOfParameters(int numberOfParameters) {
            this.numberOfParameters = numberOfParameters;
            return this;
        }

		public OptionBuilder setDescription(String description) {
			this.description = description;
            return this;
		}

        public OptionBuilder conflictWith(String name) {
            conflicts.add(name);
            return this;
        }

        public OptionBuilder alias(String alias) {
            this.aliases.add(alias);
            return this;
        }

        public Option build() {
            if(Objects.equals(name, "")) {
                throw new ParseException("Name of the option should be initialized");
            }
            if(numberOfParameters == -1) {
                throw new ParseException("Number of parameter should be initialized");
            }
            if(action == null) {
                this.action = it -> {};
            }
            return new Option(this);
        }
    }
    

    private Option(OptionBuilder builder) {
        this.name = builder.name;
        this.action = builder.action;
        this.numberOfParameters = builder.numberOfParameters;
        this.mandatory = builder.mandatory;
        this.aliases = builder.aliases;
        this.description = builder.description;
        this.conflicts = builder.conflicts;
    }

    public static OptionBuilder oneIntParameter(String optionName, int numberOfParameters, IntConsumer action) {
        return new OptionBuilder(
                optionName,
                numberOfParameters,
                arguments -> action.accept(Integer.parseInt(arguments.get(0))));
    }

    public static OptionBuilder twoIntParameter(String optionName, int numberOfParameters, BiConsumer<Integer, Integer> action) {
        return new OptionBuilder(
                optionName,
                numberOfParameters,
                arguments -> action.accept(Integer.parseInt(arguments.get(0)), Integer.parseInt(arguments.get(1))));
    }

    public static OptionBuilder oneInetSocketParameter(String optionName, int numberOfParameters, Consumer<InetSocketAddress> action) {
        return new OptionBuilder(
                optionName,
                numberOfParameters,
                arguments -> action.accept(new InetSocketAddress(arguments.get(0), Integer.parseInt(arguments.get(1)))));
    }

    public static OptionBuilder createBuilder(String name, int numberOfParameters, Consumer<List<String>> action) {
        return new OptionBuilder(name, numberOfParameters, action);
    }

    public static boolean isOption(String argument) {
        Objects.requireNonNull(argument);
        return argument.toCharArray()[0] == '-';
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public int getNumberOfParameters() {
    	return numberOfParameters;
    }

    public boolean containsAlias(String alias) {
        return aliases.contains(alias);
    }

	public List<String> getAliases() {
		return aliases.stream().toList();
	}

	public String getDescription() {
		return description;
	}

    public void accept(List<String> parameters) {
        this.action.accept(parameters);
    }

    public HashSet<String> getConflicts() {
        return new HashSet<>(conflicts);
    }

    @Override
    public String toString() {
        var aliasesMessage = new StringJoiner(", ", this.name, "");
        aliases.forEach(aliasesMessage::add);
        return aliasesMessage + "\t\t" + description;
    }
}
