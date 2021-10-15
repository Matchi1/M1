package fr.uge.poo.paint.ex9;

public interface Canvas {
    public void clearWindow(CustomColor color);
    public void drawLine(int x1, int y1, int x2, int y2, CustomColor color);
    public void drawRectangle(int x, int y, int width, int height, CustomColor color);
    public void drawEllipse(int x, int y, int width, int height, CustomColor color);
    public void waitForMouseEvents(MouseAdapter mouseCb);
    public void refresh();
}
