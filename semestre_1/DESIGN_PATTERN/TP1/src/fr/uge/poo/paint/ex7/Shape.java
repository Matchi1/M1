package fr.uge.poo.paint.ex7;

import fr.uge.poo.paint.ex7.ColorManager.CustomColor;

public interface Shape {
	public void draw(GraphicManager graphics, CustomColor Color);
	public int distance(int x, int y);
	public int windowWidthMin();
	public int windowHeightMin();
}
