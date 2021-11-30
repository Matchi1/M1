package fr.uge.poo.cmdline.ex7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionBuilderTest {
    @Test
    public void processRequiredOption() {
        var cmdParser = new CmdLineParser();
        var option= new Option.OptionBuilder("-test",0, l->{}).required().build();
        cmdParser.addOption(option);
        cmdParser.addFlag("-test1",() -> {});
        String[] arguments = {"-test1","a","b"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflicts() {
        var cmdParser = new CmdLineParser();
        var option= new Option.OptionBuilder("-test",0, l->{}).conflictWith("-test1").build();
        cmdParser.addOption(option);
        var option2= new Option.OptionBuilder("-test1",0, l->{}).build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test","-test1"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflicts2() {
        var cmdParser = new CmdLineParser();
        var option= new Option.OptionBuilder("-test",0, l->{}).conflictWith("-test1").build();
        cmdParser.addOption(option);
        var option2= new Option.OptionBuilder("-test1",0, l->{}).build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test1","-test"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflictsAndAliases() {
        var cmdParser = new CmdLineParser();
        var option= new Option.OptionBuilder("-test",0, l->{}).conflictWith("-test2").build();
        cmdParser.addOption(option);
        var option2= new Option.OptionBuilder("-test1",0, l->{}).alias("-test2").build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test1","-test"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflictsAndAliases2() {
        var cmdParser = new CmdLineParser();
        var option= new Option.OptionBuilder("-test",0, l->{}).conflictWith("-test2").build();
        cmdParser.addOption(option);
        var option2= new Option.OptionBuilder("-test1",0, l->{}).alias("-test2").build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test","-test1"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }
}
