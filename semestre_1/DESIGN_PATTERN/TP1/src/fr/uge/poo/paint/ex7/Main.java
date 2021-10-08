package fr.uge.poo.paint.ex7;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
		try {
			Paint paint = new Paint();
			paint.initFigures("draw2.txt");
			Point windowSize = paint.minDimension();
			var widthMin = 500;
			var heightMin = 500;
			if(windowSize.x() > 500) {
				widthMin = windowSize.x();
			}
			if(windowSize.y() > 500) {
				heightMin = windowSize.y();
			}
			GraphicManager area = new GraphicManager("area", widthMin, heightMin, 0);
			paint.paintAll(area);
			area.waitForMouseEvents((x, y) -> paint.mouse_cb(area, x, y));
		} catch (IOException e) {
            System.err.println(e);
		}
	}
}
