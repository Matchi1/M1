package fr.uge.poo.paint.ex9;

public interface Shape {
	public void draw(Canvas graphics, CustomColor Color);
	public int distance(int x, int y);
	public int windowWidthMin();
	public int windowHeightMin();
}
