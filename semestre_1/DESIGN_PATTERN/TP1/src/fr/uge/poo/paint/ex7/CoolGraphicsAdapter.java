package fr.uge.poo.paint.ex7;

import fr.uge.poo.coolgraphics.CoolGraphics;

import java.util.Objects;

public class CoolGraphicsAdapter implements Graphic {
    private final CoolGraphics graphics;

    public CoolGraphicsAdapter(String title, int width, int height) {
        Objects.requireNonNull(title);
        this.graphics = new CoolGraphics(title, width, height);
    }

    public void clearWindow(CustomColor color) {
        Objects.requireNonNull(color);
        graphics.repaint(color.getColorPlus());
    }

    public void drawLine(int x1, int y1, int x2, int y2, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.drawLine(x1, y1, x2, y2, color.getColorPlus());
    }

    public void drawRectangle(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.drawLine(x, y, x + width, y, color.getColorPlus());
        graphics.drawLine(x, y, x, y + height, color.getColorPlus());
        graphics.drawLine(x + width, y, x + width, y + height, color.getColorPlus());
        graphics.drawLine(x, y + height, x + width, y + height, color.getColorPlus());
    }

    public void drawEllipse(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.drawEllipse(x, y, width, height, color.getColorPlus());
    }

    public void waitForMouseEvents(MouseAdapter mouseCb) {
        graphics.waitForMouseEvents(mouseCb::mouseClicked);
    }
}
