package fr.uge.poo.paint.ex6;

import java.awt.Graphics2D;

public interface Shape {
	public void draw(Graphics2D graphics);
	public int distance(int x, int y);
	public int windowWidthMin();
	public int windowHeightMin();
}
