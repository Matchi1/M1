package fr.uge.poo.paint.ex7;

import fr.uge.poo.paint.ex8.CanvasFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) throws IOException {
		var paint = new Paint();
		paint.initFigures("draw-big.txt");
		var windowSize = paint.minDimension();
		var widthMin = windowSize.x() > 500 ? windowSize.x() : 500;
		var heightMin = windowSize.y() > 500 ? windowSize.y() : 500;
		var loader = ServiceLoader.load(CanvasFactory.class);
		var factory = loader.findFirst();
		Canvas area;
		if(Arrays.stream(args).toList().contains("-legacy")) {
			area = new SimpleGraphicsAdapter("area", widthMin, heightMin);
		} else {
			area = new CoolGraphicsAdapter("area", widthMin, heightMin);
		}
		paint.paintAll(area);
		area.waitForMouseEvents((x, y) -> paint.mouse_cb(area, x, y));
	}
}
