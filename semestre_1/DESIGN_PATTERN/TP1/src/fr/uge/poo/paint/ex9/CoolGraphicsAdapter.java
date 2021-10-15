package fr.uge.poo.paint.ex9;

import fr.uge.poo.coolgraphics.CoolGraphics;
import fr.uge.poo.coolgraphics.CoolGraphics.ColorPlus;

import java.util.Objects;

public class CoolGraphicsAdapter implements Canvas {
    private final CoolGraphics graphics;

    public CoolGraphicsAdapter(String title, int width, int height) {
        Objects.requireNonNull(title);
        this.graphics = new CoolGraphics(title, width, height);
    }

    private ColorPlus convertColor(CustomColor color) {
        Objects.requireNonNull(color);
        return switch(color) {
            case BLACK -> ColorPlus.BLACK;
            case WHITE -> ColorPlus.WHITE;
            case ORANGE ->  ColorPlus.ORANGE;
        };
    }

    public void clearWindow(CustomColor color) {
        Objects.requireNonNull(color);
        graphics.repaint(convertColor(color));
    }

    public void drawLine(int x1, int y1, int x2, int y2, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.drawLine(x1, y1, x2, y2, convertColor(color));
    }

    public void drawRectangle(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        var colorplus = convertColor(color);
        graphics.drawLine(x, y, x + width, y, colorplus);
        graphics.drawLine(x, y, x, y + height, colorplus);
        graphics.drawLine(x + width, y, x + width, y + height, colorplus);
        graphics.drawLine(x, y + height, x + width, y + height, colorplus);
    }

    public void drawEllipse(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        graphics.drawEllipse(x, y, width, height, convertColor(color));
    }

    public void waitForMouseEvents(MouseAdapter mouseCb) {
        graphics.waitForMouseEvents(mouseCb::mouseClicked);
    }

    @Override
    public void refresh() {}
}
