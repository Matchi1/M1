package fr.uge.poo.cmdline.ex6;

import java.util.*;
import java.util.function.Consumer;

public class Option {
    private final String name;
    private final int numberOfParameters;
    private final boolean mandatory;
    private final Consumer<List<String>> action;
    private final HashSet<String> aliases;
	private final String description;

    public static class OptionBuilder {
        private String name = "";
        private int numberOfParameters = -1;
        private boolean mandatory = false;
        private Consumer<List<String>> action = null;
        private HashSet<String> aliases = new HashSet<>();
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

        public OptionBuilder setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
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

        public void addAlias(String alias) {
            this.aliases.add(alias);
        }

        public Option build() {
            if(Objects.equals(name, "")) {
                throw new IllegalStateOptions("Name of the option should be initialized");
            }
            if(numberOfParameters == -1) {
                throw new IllegalStateOptions("Number of parameter should be initialized");
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
}
