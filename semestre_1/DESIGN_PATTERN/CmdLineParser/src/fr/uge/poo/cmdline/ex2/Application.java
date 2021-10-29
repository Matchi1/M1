package fr.uge.poo.cmdline.ex2;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    static private class PaintSettings {
        private boolean legacy=false;
        private boolean bordered=true;

        public void setLegacy(boolean legacy) {
            this.legacy = legacy;
        }

        public boolean isLegacy(){
            return legacy;
        }

        public void setBordered(boolean bordered){
            this.bordered=bordered;
        }

        public boolean isBordered(){
            return bordered;
        }

        @Override
        public String toString(){
            return "PaintSettings [ bordered = "+bordered+", legacy = "+ legacy +" ]";
        }
    }

    public static void main(String[] args) {
        var options = new PaintSettings();
        String[] arguments={"-legacy","-no-borders","filename1","filename2","-border-width","100","-window-name","area"};
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> options.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> options.setBordered(true));
        cmdParser.addFlag("-no-borders", () -> options.setBordered(false));
        cmdParser.addFlagWithParameter("-border-width", width -> System.out.println(width));
        cmdParser.addFlagWithParameter("-window-name", name -> System.out.println(name));
        var result = cmdParser.process(arguments);
        var files = result.stream().map(Path::of).collect(Collectors.toList());
        files.forEach(System.out::println);
        System.out.println(options.toString());
    }
}
