package fr.uge.poo.cmdline.ex6;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void addFlagWithParameterShouldFailFastOnNullArguments() {
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.addFlagWithParameter(null, null));
    }

    @Test
    public void processReturnCorrectNumberOfFiles() {
        var parser = new CmdLineParser();
        String[] arguments = {"filename1", "-opt", "filename2", "hello"};
        parser.addFlag("-opt", () -> {});
        var files = parser.process(arguments);
        assertEquals(files.size(), 3);
    }

    @Test
    public void processReturnCorrectArgument() {
        var parser = new CmdLineParser();
        String[] arguments = {"filename1", "-opt", "filename2", "hello"};
        String[] expectedFiles = {"filename1", "filename2", "hello"};
        parser.addFlag("-opt", () -> {});
        var files = parser.process(arguments);
        assertEquals(List.of(expectedFiles), files);
    }

    @Test
    public void processReturnCorrectArgumentWithParameter() {
        var parser = new CmdLineParser();
        String[] arguments = {"filename1", "-opt", "filename2", "hello"};
        String[] expectedFiles = {"filename1", "hello"};
        parser.addFlagWithParameter("-opt", argument -> {});
        var files = parser.process(arguments);
        assertEquals(List.of(expectedFiles), files);
    }

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
        assertEquals(List.of(expectedRunOrder), runOrder);
    }

    @Test
    public void shouldReturnCorrectFilesWithAddFlagWithParameter() {
        var parser = new CmdLineParser();
        var runOrder = new ArrayList<String>();
        String[] arguments = {"filename1", "-opt", "filename2", "-opt1", "hello", "-legacy"};
        String[] expectedParameters = {"filename2", "hello"};
        parser.addFlag("-legacy", () -> {});
        parser.addFlagWithParameter("-opt", runOrder::add);
        parser.addFlagWithParameter("-opt1", runOrder::add);
        parser.process(arguments);
        assertEquals(List.of(expectedParameters), runOrder);
    }

    @Test
    public void shouldThrowIllegalStateOption() {
        var parser = new CmdLineParser();
        String[] arguments = {"-window-name", "-border-width", "500", "filename"};
        parser.addFlagWithParameter("-window-name", argument -> {});
        parser.addFlagWithParameter("-border-width", argument -> {});
        assertThrows(IllegalStateOptions.class, () -> parser.process(arguments));
    }

    @Test
    public void shouldThrowIllegalStateOptionWithMandatoryOption() {
        var parser = new CmdLineParser();
        String[] arguments = {"-border-width", "500", "filename"};
        parser.addOption("-window-name", argument -> {}, 1, true);
        parser.addFlagWithParameter("-border-width", argument -> {});
        assertThrows(IllegalStateOptions.class, () -> parser.process(arguments));
    }

    @Test
    public void shouldThrowIllegalStateOptionNotEnoughArgument() {
        var parser = new CmdLineParser();
        String[] arguments = {"-import", "file1", "file2"};
        parser.addOption("-import", list -> {}, 3, false);
        assertThrows(IllegalStateOptions.class, () -> parser.process(arguments));
    }

    @Test
    public void shouldBeSuccessfulWithMultipleParameters() {
        var parser = new CmdLineParser();
        String[] arguments = {"-import", "file1", "file2", "file3"};
        parser.addOption("-import", list -> {}, 3, false);
        assertEquals(new ArrayList<>(), parser.process(arguments));
    }

    @Test
    public void optionShouldReadArgumentInOrder() {
        var parser = new CmdLineParser();
        String[] expectedOrder = {"file1", "file2", "file3"};
        String[] arguments = {"-import", "file1", "file2", "file3"};
        parser.addOption("-import", list -> {
            assertEquals(list, List.of(expectedOrder));
        }, 3, false);
        parser.process(arguments);
    }

    @Test
    public void shouldThrowIllegalStateOptionsWhenNotEnoughParameters() {
        var parser = new CmdLineParser();
        String[] arguments = {"-import", "file1", "file2"};
        parser.addOption("-import", list -> {}, 3, false);
        assertThrows(IllegalStateOptions.class, () -> parser.process(arguments));
    }
}