package fr.uge.poo.paint.ex3;

import java.awt.Color;
import java.awt.Graphics2D;

public class Rectangle implements Shape {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, width, height);
	}
}
