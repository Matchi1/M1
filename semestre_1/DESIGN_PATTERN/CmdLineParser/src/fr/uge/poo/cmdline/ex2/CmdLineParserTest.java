package fr.uge.poo.cmdline.ex2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("static-method")
class CmdLineParserTest {

    @Test
    public void processShouldFailFastOnNullArgument(){
        var parser = new fr.uge.poo.cmdline.ex1.CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.process(null));
    }

    @Test
    public void addFlagShouldFailOnNullArgument() {
        var parser = new fr.uge.poo.cmdline.ex1.CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.addFlag(null, null));
    }

    @Test
    public void addFlagWithParameterShouldFailFastOnNullArguments() {
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.addFlagWithParameter(null, null));
    }

    @Test
    public void processReturnCorrectNumberOfFiles() {
        var parser = new fr.uge.poo.cmdline.ex1.CmdLineParser();
        String[] arguments = {"filename1", "-opt", "filename2", "hello"};
        parser.addFlag("-opt", () -> System.out.println("hello"));
        var files = parser.process(arguments);
        assertEquals(files.size(), 3);
    }

    @Test
    public void processReturnCorrectArgument() {
        var parser = new CmdLineParser();
        String[] arguments = {"filename1", "-opt", "filename2", "hello"};
        String[] expectedFiles = {"filename1", "filename2", "hello"};
        parser.addFlag("-opt", () -> System.out.println("hello"));
        var files = parser.process(arguments);
        for(var i = 0; i < files.size(); i++){
            assertEquals(files.get(i), expectedFiles[i]);
        }
    }

    @Test
    public void processReturnCorrectArgumentWithParameter() {
        var parser = new CmdLineParser();
        String[] arguments = {"filename1", "-opt", "filename2", "hello"};
        String[] expectedFiles = {"filename1", "hello"};
        parser.addFlagWithParameter("-opt", argument -> {});
        var files = parser.process(arguments);
        for(var i = 0; i < files.size(); i++){
            assertEquals(files.get(i), expectedFiles[i]);
        }
    }
<<<<<<< Updated upstream

    @Test
    public void runnableCorrectlyExecuted() {
        var parser = new CmdLineParser();
        var runExecuted = new ArrayList<Integer>();
        String[] arguments = {"filename1", "-opt", "filename2", "hello", "-legacy"};
        parser.addFlag("-legacy", () -> runExecuted.add(0));
        parser.addFlagWithParameter("-opt", argument -> runExecuted.add(1));
        parser.process(arguments);
        assertEquals(runExecuted.size(), 2);
    }

    @Test
    public void runnableExecutionOrder() {
        var parser = new CmdLineParser();
        var runOrder = new ArrayList<Integer>();
        String[] arguments = {"filename1", "-opt", "filename2", "hello", "-legacy"};
        Integer[] expectedRunOrder = {1, 0};
        parser.addFlag("-legacy", () -> runOrder.add(0));
        parser.addFlagWithParameter("-opt", argument -> runOrder.add(1));
        parser.process(arguments);
        assertEquals(runOrder.size(), expectedRunOrder.length);
        for(var i = 0; i < runOrder.size(); i++){
            assertEquals(runOrder.get(i), expectedRunOrder[i]);
        }
    }

    @Test
    public void parameterSaved() {
        var parser = new CmdLineParser();
        var runOrder = new ArrayList<String>();
        String[] arguments = {"filename1", "-opt", "filename2", "-opt1", "hello", "-legacy"};
        String[] expectedParameters = {"filename2", "hello"};
        parser.addFlag("-legacy", () -> {});
        parser.addFlagWithParameter("-opt", runOrder::add);
        parser.addFlagWithParameter("-opt1", runOrder::add);
        parser.process(arguments);
        assertEquals(runOrder.size(), expectedParameters.length);
        for(var i = 0; i < runOrder.size(); i++){
            assertEquals(runOrder.get(i), expectedParameters[i]);
        }
    }
=======
>>>>>>> Stashed changes
}