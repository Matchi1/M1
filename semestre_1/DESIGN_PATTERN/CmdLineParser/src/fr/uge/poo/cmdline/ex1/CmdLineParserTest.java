package fr.uge.poo.cmdline.ex1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("static-method")
class CmdLineParserTest {

    @Test
    public void processShouldFailFastOnNullArgument(){
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.process(null));
    }

    @Test
    public void addFlagShouldFailOnNullArgument() {
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.addFlag(null, null));
    }

    @Test
    public void processReturnCorrectNumberOfFiles() {
        var parser = new CmdLineParser();
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
}