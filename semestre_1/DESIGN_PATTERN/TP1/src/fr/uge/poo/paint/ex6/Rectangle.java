package fr.uge.poo.paint.ex6;

import java.awt.Graphics2D;

public class Rectangle extends abstractShape {
	public Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void draw(Graphics2D graphics) {
        graphics.drawRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
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
		Rectangle rectangle = (Rectangle) obj;
		if(rectangle.getX() != super.getX() || rectangle.getY() != super.getY()) {
			return false;
		}
		if(rectangle.getWidth() != super.getWidth() || rectangle.getHeight() != super.getHeight()) {
			return false;
		}
		return true;
	}
}
