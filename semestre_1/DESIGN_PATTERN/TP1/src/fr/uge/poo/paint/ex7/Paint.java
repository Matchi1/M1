package fr.uge.poo.paint.ex7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

import fr.uge.poo.paint.ex7.ColorManager.CustomColor;

public class Paint {
	private final ArrayList<Shape> shapes;
	private Shape particularShape;
	
	public Paint() {
		this.shapes = new ArrayList<>();
	}
	
	private void addShapeFromString(String line) {
		String[] tokens = line.split(" ");
	    int x1 = Integer.parseInt(tokens[1]);
	    int y1 = Integer.parseInt(tokens[2]);
	    int x2 = Integer.parseInt(tokens[3]);
	    int y2 = Integer.parseInt(tokens[4]);
	    switch(tokens[0]) {
			case "ellipse":
				shapes.add(new Ellipse(x1, y1, x2, y2));
				break;
			case "rectangle":
				shapes.add(new Rectangle(x1, y1, x2, y2));
				break;
			default:
				shapes.add(new Line(x1, y1, x2, y2));
	    }
	}
	
	public void initFigures(String pathName) throws IOException {
		Path path = Paths.get(pathName);
		Stream<String> lines = Files.lines(path);
		lines.forEach(line -> addShapeFromString(line));
		lines.close();
	}
	
	public void paintAll(GraphicManager area) {
		area.clearWindow(CustomColor.WHITE);
		shapes.forEach(s -> {
			if(s.equals(particularShape)) {
				particularShape.draw(area, CustomColor.ORANGE);
			} else {
				s.draw(area, CustomColor.BLACK);
			}
		});
	}

	public void mouse_cb(GraphicManager area, int x, int y) {
		Comparator<Shape> c = (s1, s2) -> {
			if (s1.distance(x, y) > s2.distance(x, y)) {
				return 1;
			} else if (s1.distance(x, y) == s2.distance(x, y)) {
				return 0;
			} else {
				return -1;
			}
		};
		if (!shapes.isEmpty()) {
			var sortedShape = this.shapes.stream().sorted(c).toList();
			this.particularShape = sortedShape.get(0);
			this.paintAll(area);
		}
	}
	
	public Point minDimension() {
		var widthMin = 0;
		var heightMin = 0;
		for(var shape : shapes) {
			if(shape.windowHeightMin() > heightMin) {
				heightMin = shape.windowHeightMin();
			}
			if(shape.windowWidthMin() > widthMin) {
				widthMin = shape.windowWidthMin();
			}
		}
		return new Point(widthMin, heightMin);
	}
}
