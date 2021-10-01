package fr.uge.poo.paint.ex2;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import fr.uge.poo.simplegraphics.SimpleGraphics;

public class Paint {
	private ArrayList<Line> figures;
	
	public Paint() {
		this.figures = new ArrayList<>();
	}
	
	private void addLineFromString(String line) {
		String[] tokens = line.split(" ");
	    int x1 = Integer.parseInt(tokens[1]);
	    int y1 = Integer.parseInt(tokens[2]);
	    int x2 = Integer.parseInt(tokens[3]);
	    int y2 = Integer.parseInt(tokens[4]);
	    figures.add(new Line(x1, y1, x2, y2));
	}
	
	private void initFigures(String pathName) throws IOException {
		Path path = Paths.get(pathName);
		try(Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> addLineFromString(line));
		}
		
	}
	
	public static void paintAll(SimpleGraphics area, List<Line> list) {
		area.clear(Color.WHITE);
		area.render(graphics -> {
			graphics.setColor(Color.BLACK);
			list.forEach(line -> line.draw(graphics));
		});
	}
	
	public static void main(String[] args) {
		SimpleGraphics area = new SimpleGraphics("area", 800, 600);
		Paint paint = new Paint();
		try {
			paint.initFigures("draw1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		paintAll(area, paint.figures);
	}
}
