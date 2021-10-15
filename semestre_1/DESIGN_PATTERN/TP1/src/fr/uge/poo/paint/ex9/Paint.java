package fr.uge.poo.paint.ex9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class Paint {
	private final ArrayList<Shape> shapes = new ArrayList<>();
	private Shape particularShape;
	
	private void addShapeFromString(String line) {
		var tokens = line.split(" ");
	    var x1 = Integer.parseInt(tokens[1]);
	    var y1 = Integer.parseInt(tokens[2]);
	    var x2 = Integer.parseInt(tokens[3]);
	    var y2 = Integer.parseInt(tokens[4]);
		switch (tokens[0]) {
			case "ellipse" -> shapes.add(new Ellipse(x1, y1, x2, y2));
			case "rectangle" -> shapes.add(new Rectangle(x1, y1, x2, y2));
			default -> shapes.add(new Line(x1, y1, x2, y2));
		}
	}
	
	public void initFigures(String pathName) throws IOException {
		var path = Paths.get(pathName);
		var lines = Files.lines(path);
		lines.forEach(this::addShapeFromString);
		lines.close();
	}
	
	public void paintAll(Canvas area) {
		area.clearWindow(CustomColor.WHITE);
		shapes.forEach(s -> {
			if(s.equals(particularShape)) {
				particularShape.draw(area, CustomColor.ORANGE);
			} else {
				s.draw(area, CustomColor.BLACK);
			}
		});
		area.refresh();
	}

	public void mouse_cb(Canvas area, int x, int y) {
		if (!shapes.isEmpty()) {
			 particularShape = shapes.stream()
			 		.min(Comparator.comparingInt(s -> s.distance(x, y)))
			 		.get();
		}
		paintAll(area);
	}
	
	public Point minDimension() {
		var widthMin = 0;
		var heightMin = 0;
		for(var shape : shapes) {
			heightMin = Integer.max(shape.windowHeightMin(), heightMin);
			widthMin = Integer.max(shape.windowWidthMin(), widthMin);
		}
		return new Point(widthMin, heightMin);
	}
}
