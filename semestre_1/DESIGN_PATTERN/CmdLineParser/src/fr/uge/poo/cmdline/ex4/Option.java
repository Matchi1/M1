package fr.uge.poo.cmdline.ex4;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Option {
    private final String name;
    private final int numberOfParameters;
    private final boolean mandatory;
    private final Consumer<List<String>> action;

    public static class OptionBuilder {
        private String name = "";
        private int numberOfParameters = -1;
        private boolean mandatory = false;
        private Consumer<List<String>> action = null;
        
        public OptionBuilder(String name, int numberOfParameters, Consumer<List<String>> action) {
			this.name = name;
			this.numberOfParameters = numberOfParameters;
			this.action = action;
		}

        public void setName(String option) {
            this.name = option;
        }

        public void setCode(Consumer<List<String>> action) {
            this.action = action;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }

        public void setNumberOfParameters(int numberOfParameters) {
            this.numberOfParameters = numberOfParameters;
        }

        public Option build() {
            if(Objects.equals(name, "")) {
                throw new IllegalStateOptions("Name of the option should be initialized");
            } else if(numberOfParameters == -1) {
                throw new IllegalStateOptions("Number of parameter should be initialized");
            } else if(action == null) {
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
    }

    public static OptionBuilder createBuilder(String name, int numberOfParameters, Consumer<List<String>> action) {
        return new OptionBuilder(name, numberOfParameters, action);
    }

    public static boolean isOption(String argument) {
        Objects.requireNonNull(argument);
        return argument.toCharArray()[0] == '-';
    }

    public boolean isMandatory() {
        return mandatory;
    }
    
    public int getNumberOfParameters() {
    	return numberOfParameters;
    }

    public void accept(List<String> parameters) {
        this.action.accept(parameters);
    }
}
