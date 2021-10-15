package fr.uge.poo.paint.ex9;

import java.util.Objects;

public class Ellipse extends abstractShape {
	public Ellipse(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void draw(Canvas graphics, CustomColor color) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(color);
        graphics.drawEllipse(super.getX(), super.getY(), super.getWidth(), super.getHeight(), color);
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
		return ellipse.getWidth() == super.getWidth() && ellipse.getHeight() == super.getHeight();
	}
}
