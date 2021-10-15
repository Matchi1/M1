package fr.uge.poo.paint.ex8;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.awt.*;
import java.util.Objects;

public class SimpleGraphicsAdapter implements Canvas {
    private final SimpleGraphics graphics;

    public SimpleGraphicsAdapter(String title, int width, int height) {
        Objects.requireNonNull(title);
        this.graphics = new SimpleGraphics(title, width, height);
    }

    private Color convertColor(CustomColor color) {
        Objects.requireNonNull(color);
        return switch(color) {
            case BLACK -> Color.BLACK;
            case WHITE -> Color.WHITE;
            case ORANGE ->  Color.ORANGE;
        };
    }

    public void clearWindow(CustomColor color) {
        Objects.requireNonNull(color);
        graphics.clear(convertColor(color));
    }

    public void drawLine(int x1, int y1, int x2, int y2, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.render(area -> {
            area.setColor(convertColor(color));
            area.drawLine(x1, y1, x2, y2);
        });
    }

    public void drawRectangle(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.render(area -> {
            area.setColor(convertColor(color));
            area.drawRect(x, y, width, height);
        });
    }

    public void drawEllipse(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.render(area -> {
            area.setColor(convertColor(color));
            area.drawOval(x, y, width, height);
        });
    }

    public void waitForMouseEvents(MouseAdapter mouseCb) {
        graphics.waitForMouseEvents(mouseCb::mouseClicked);
    }
}
