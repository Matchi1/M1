package fr.uge.poo.paint.ex8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) throws IOException {
		var paint = new Paint();
		paint.initFigures("draw-big.txt");
		var windowSize = paint.minDimension();
		var widthMin = windowSize.x() > 500 ? windowSize.x() : 500;
		var heightMin = windowSize.y() > 500 ? windowSize.y() : 500;
		var loader = ServiceLoader.load(fr.uge.poo.paint.ex8.CanvasFactory.class);
		var factory = loader.findFirst();
		Canvas area;
		if(factory.isPresent()) {
			area = factory.get().createCanvas("area", widthMin, heightMin);
		} else {
			area = new SimpleGraphicsAdapter("area", widthMin, heightMin);
		}
		paint.paintAll(area);
		area.waitForMouseEvents((x, y) -> paint.mouse_cb(area, x, y));
	}
}
