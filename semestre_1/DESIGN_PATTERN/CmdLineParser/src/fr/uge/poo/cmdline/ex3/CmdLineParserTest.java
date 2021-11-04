package fr.uge.poo.cmdline.ex3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}