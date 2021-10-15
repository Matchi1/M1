package fr.uge.poo.paint.ex8;

public abstract class abstractShape implements Shape {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public abstractShape(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
	public int windowWidthMin() {
		return x + width;
	}
	
	public int windowHeightMin() {
		return y + height;
	}

	@Override
	public int distance(int x, int y) {
		int distX = this.x + width / 2 - x;
		int distY = this.y + height / 2 - y;
		return distX * distX + distY * distY;
	}
}
