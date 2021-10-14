package fr.uge.poo.paint.ex7;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.util.Objects;

public class SimpleGraphicsAdapter implements Graphic {
    private final SimpleGraphics graphics;

    public SimpleGraphicsAdapter(String title, int width, int height) {
        Objects.requireNonNull(title);
        this.graphics = new SimpleGraphics(title, width, height);
    }

    public void clearWindow(CustomColor color) {
        Objects.requireNonNull(color);
        graphics.clear(color.getColor());
    }

    public void drawLine(int x1, int y1, int x2, int y2, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.render(area -> {
            area.setColor(color.getColor());
            area.drawLine(x1, y1, x2, y2);
        });
    }

    public void drawRectangle(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.render(area -> {
            area.setColor(color.getColor());
            area.drawRect(x, y, width, height);
        });
    }

    public void drawEllipse(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.render(area -> {
            area.setColor(color.getColor());
            area.drawOval(x, y, width, height);
        });
    }

    public void waitForMouseEvents(MouseAdapter mouseCb) {
        graphics.waitForMouseEvents(mouseCb::mouseClicked);
    }
}
