package fr.uge.poo.cmdline.ex4;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class Option {
    private final String option;
    private final int numberOfParameters;
    private final boolean mandatory;
    private final Consumer<Iterator<String>> code;

    public static class OptionBuilder {
        private String option = "";
        private int numberOfParameters = -1;
        private boolean mandatory = false;
        private Consumer<Iterator<String>> code = null;

        public void setOption(String option) {
            this.option = option;
        }

        public void setCode(Consumer<Iterator<String>> code) {
            this.code = code;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }

        public void setNumberOfParameters(int numberOfParameters) {
            this.numberOfParameters = numberOfParameters;
        }

        public Option build() {
            if(Objects.equals(option, "")) {
                throw new IllegalStateOptions("Name of the option should be initialized");
            } else if(numberOfParameters == -1) {
                throw new IllegalStateOptions("Number of parameter should be initialized");
            } else if(code == null) {
                this.code = it -> {};
            }
            return new Option(this);
        }
    }

    private Option(OptionBuilder builder) {
        this.option = builder.option;
        this.code = builder.code;
        this.numberOfParameters = builder.numberOfParameters;
        this.mandatory = builder.mandatory;
    }

    public static OptionBuilder builder() {
        return new OptionBuilder();
    }

    public static boolean isOption(String argument) {
        Objects.requireNonNull(argument);
        return argument.toCharArray()[0] == '-';
    }

    public boolean isMandatory() {
        return this.mandatory;
    }

    public void accept(Iterator<String> parameters) {
        this.code.accept(parameters);
    }
}
