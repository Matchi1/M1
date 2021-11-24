package fr.uge.poo.cmdline.ex5;

import java.nio.file.Path;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        var paintSettingBuilder = PaintSettings.getPaintSettingBuilder();
        String[] arguments = {"-legacy", "-no-borders", "filename1", "filename2", "-border-width", "100",
                "-window-name", "area", "-h"};
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> paintSettingBuilder.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> paintSettingBuilder.setBordered(true));
        cmdParser.addFlag("-no-borders", () -> paintSettingBuilder.setBordered(false));
        cmdParser.addFlagWithParameter("-border-width", argument -> paintSettingBuilder.setBorderWidth(Integer.parseInt(argument)));
        cmdParser.addFlagWithParameter("-window-name", paintSettingBuilder::setWindowName);
        var result = cmdParser.process(arguments);
        var files = result.stream().map(Path::of).collect(Collectors.toList());
        files.forEach(System.out::println);
        var options = paintSettingBuilder.build();
        System.out.println(options.toString());
    }
}
