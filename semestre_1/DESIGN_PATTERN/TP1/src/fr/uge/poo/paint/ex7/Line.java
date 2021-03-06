package fr.uge.poo.paint.ex7;

import java.util.Objects;

public class Line implements Shape {
	private final int x0;
	private final int y0;
	private final int x1;
	private final int y1;
	
	public Line(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void draw(Canvas graphics, CustomColor color) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(color);
        graphics.drawLine(x0, y0, x1, y1, color);
	}
	
	public int windowWidthMin() {
		if(x0 < x1) {
			return x1;
		}
		return x0;
	}
	
	public int windowHeightMin() {
		if(y0 < y1) {
			return y1;
		}
		return y0;
	}

	@Override
	public int distance(int x, int y) {
		int dist_x = (this.x0 + this.x1) / 2 - x;
		int dist_y = (this.y0 + this.y1) / 2 - y;
		return dist_x * dist_x + dist_y * dist_y;
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
		Line line = (Line) obj;
		if(line.x0 != x0 || line.x1 != x1) {
			return false;
		}
		if(line.y0 != y0 || line.y1 != y1) {
			return false;
		}
		return true;
	}
}
