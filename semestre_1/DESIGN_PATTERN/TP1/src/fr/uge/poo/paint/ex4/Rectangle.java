package fr.uge.poo.paint.ex4;

import java.awt.Color;
import java.awt.Graphics2D;

public class Rectangle extends abstractShape {
	public Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
        graphics.drawRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
	}

}
