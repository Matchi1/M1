package fr.uge.poo.paint.ex7;

import fr.uge.poo.paint.ex7.CustomColor;

public interface Shape {
	public void draw(Graphic graphics, CustomColor Color);
	public int distance(int x, int y);
	public int windowWidthMin();
	public int windowHeightMin();
}
