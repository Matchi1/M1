package fr.uge.poo.paint.ex7;

import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
		try {
			var legacy = false;
			var paint = new Paint();
			paint.initFigures("draw-big.txt");
			var windowSize = paint.minDimension();
			var widthMin = 500;
			var heightMin = 500;
			if(windowSize.x() > 500) {
				widthMin = windowSize.x();
			}
			if(windowSize.y() > 500) {
				heightMin = windowSize.y();
			}
			for(var arg : args) {
				if(arg.equals("-legacy")) {
					legacy = true;
				}
			}
			Graphic area;
			if(legacy) {
				area = new SimpleGraphicsAdapter("area", widthMin, heightMin);
			} else {
				area = new CoolGraphicsAdapter("area", widthMin, heightMin);
			}
			paint.paintAll(area);
			area.waitForMouseEvents((x, y) -> paint.mouse_cb(area, x, y));
		} catch (IOException e) {
            System.err.println(e);
		}
	}
}
