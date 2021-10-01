package fr.uge.poo.paint.ex5;

import java.awt.Graphics2D;

public interface Shape {
	public void draw(Graphics2D graphics);
	public int distance(int x, int y);
	boolean equals(Object obj);
}
