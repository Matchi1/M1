package fr.uge.poo.paint.ex6;

import java.io.IOException;
import fr.uge.poo.simplegraphics.SimpleGraphics;

public class Main {
    public static void main(String[] args) {
		Paint paint = new Paint();
		try {
			paint.initFigures("draw-big.txt");
		} catch (IOException e) {
            System.err.println(e);
		}
		Point windowSize = paint.minDimension();
		var widthMin = 500;
		var heightMin = 500;
		if(windowSize.x() > 500) {
			widthMin = windowSize.x();
		}
		if(windowSize.y() > 500) {
			heightMin = windowSize.y();
		}
		SimpleGraphics area = new SimpleGraphics("area", widthMin, heightMin);
		paint.paintAll(area);
		area.waitForMouseEvents((x, y) -> paint.mouse_cb(area, x, y));
	}
}
