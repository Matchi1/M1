package fr.uge.poo.paint.ex5;

import java.awt.Graphics2D;

public class Ellipse extends abstractShape {
	public Ellipse(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void draw(Graphics2D graphics) {
        graphics.drawOval(super.getX(), super.getY(), super.getWidth(), super.getHeight());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(obj.getClass() != getClass()) {
			return false;
		}
		Ellipse ellipse = (Ellipse) obj;
		if(ellipse.getX() != super.getX() || ellipse.getY() != super.getY()) {
			return false;
		}
		if(ellipse.getWidth() != super.getWidth() || ellipse.getHeight() != super.getHeight()) {
			return false;
		}
		return true;
	}
}
