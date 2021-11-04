package fr.uge.poo.cmdline.ex3;

import java.nio.file.Path;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        var optionsBuilder = PaintSettings.getPaintSettingBuilder();
        String[] arguments={"-legacy","-no-borders","filename1","filename2","-border-width","100","-window-name","area"};
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> optionsBuilder.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> optionsBuilder.setBordered(true));
        cmdParser.addFlag("-no-borders", () -> optionsBuilder.setBordered(false));
        cmdParser.addFlagWithParameter("-border-width", argument -> optionsBuilder.setBorderWidth(Integer.parseInt(argument)));
        cmdParser.addFlagWithParameter("-window-name", optionsBuilder::setWindowName);
        var result = cmdParser.process(arguments);
        var files = result.stream().map(Path::of).collect(Collectors.toList());
        files.forEach(System.out::println);
        var options = optionsBuilder.build();
        System.out.println(options.toString());
    }
}
