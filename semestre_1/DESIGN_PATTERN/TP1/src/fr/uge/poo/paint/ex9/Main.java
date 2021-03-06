package fr.uge.poo.paint.ex9;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
		var paint = new Paint();
		paint.initFigures("draw2.txt");
		var windowSize = paint.minDimension();
		var widthMin = Integer.max(windowSize.x(), 500);
		var heightMin = Integer.max(windowSize.y(), 500);
		Canvas canvas;
		if(Arrays.stream(args).toList().contains("-legacy")) {
			canvas = new SimpleGraphicsAdapter("area", widthMin, heightMin);
		} else {
			canvas = new CoolGraphicsAdapter("area", widthMin, heightMin);
		}
		paint.paintAll(canvas);
		canvas.waitForMouseEvents((x, y) -> paint.mouse_cb(canvas, x, y));
	}
}
