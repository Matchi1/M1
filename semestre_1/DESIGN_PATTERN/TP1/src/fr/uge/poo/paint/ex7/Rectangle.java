package fr.uge.poo.paint.ex7;

import java.util.Objects;

public class Rectangle extends abstractShape {
	public Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void draw(Graphic graphics, CustomColor color) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(color);
        graphics.drawRectangle(super.getX(), super.getY(), super.getWidth(), super.getHeight(), color);
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
