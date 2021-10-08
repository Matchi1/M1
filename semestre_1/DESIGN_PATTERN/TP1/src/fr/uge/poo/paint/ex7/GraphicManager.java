package fr.uge.poo.paint.ex7;

import fr.uge.poo.paint.ex7.ColorManager.CustomColor;
import fr.uge.poo.coolgraphics.CoolGraphics;
import fr.uge.poo.simplegraphics.SimpleGraphics;

public class GraphicManager {
	private final int selectedGraph;
	private final CoolGraphics cgrap;
	private final SimpleGraphics sgrap;
	
	// CoolGraphics : 0
	// SimpleGraphics : 1

	public GraphicManager(String title, int width, int height, int graphicType) {
		if (graphicType < 0) {
			throw new IllegalArgumentException("graphic type have to be above 0");
		}
		this.selectedGraph = graphicType;
		if (graphicType == 0) {
			this.cgrap = new CoolGraphics(title, width, height);
			this.sgrap = null;
		} else {
			this.cgrap = null;
			this.sgrap = new SimpleGraphics(title, width, height);
		}
	}

	public void clearWindow(CustomColor color) {
		if (selectedGraph == 0) {
			cgrap.repaint(color.getColorPlus());
		} else {
			sgrap.clear(color.getColor());
		}
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, CustomColor color) {
		if (selectedGraph == 0) {
			cgrap.drawLine(x1, y1, x2, y2, color.getColorPlus());
		} else {
			sgrap.render(area -> {
				area.setColor(color.getColor());
				area.drawLine(x1, y1, x2, y2);
			});
		}
	}
	
	public void drawRectangle(int x, int y, int width, int height, CustomColor color) {
		if (selectedGraph == 0) {
			cgrap.drawLine(x, y, x + width, y, color.getColorPlus());
			cgrap.drawLine(x, y, x, y + height, color.getColorPlus());
			cgrap.drawLine(x + width, y, x + width, y + height, color.getColorPlus());
			cgrap.drawLine(x, y + height, x + width, y + height, color.getColorPlus());
		} else {
			sgrap.render(area -> {
				area.setColor(color.getColor());
				area.drawRect(x, y, width, height);
			});
		}
	}

	public void drawEllipse(int x, int y, int width, int height, CustomColor color) {
		if (selectedGraph == 0) {
			cgrap.drawEllipse(x, y, width, height, color.getColorPlus());
		} else {
			sgrap.render(area -> {
				area.setColor(color.getColor());
				area.drawOval(x, y, width, height);
			});
		}
	}

	public void waitForMouseEvents(MouseAdapter.MouseCallback mouseCb) {
		if (selectedGraph == 0) {
			cgrap.waitForMouseEvents(MouseAdapter.getMouseCallbackCool(mouseCb));
		} else {
			sgrap.waitForMouseEvents(MouseAdapter.getMouseCallbackSimple(mouseCb));
		}
	}
}
