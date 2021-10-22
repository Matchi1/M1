package fr.uge.poo.paint.ex9;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.function.Consumer;

public class SimpleGraphicsAdapter implements Canvas {
    private final SimpleGraphics graphics;
    private final ArrayList<Consumer<Graphics2D>> drawing = new ArrayList<>();

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

    @Override
    public void clearWindow(CustomColor color) {
        Objects.requireNonNull(color);
        drawing.clear();
        graphics.clear(convertColor(color));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, CustomColor color) {
        Objects.requireNonNull(color);
        drawing.add(area -> {
            area.setColor(convertColor(color));
            area.drawLine(x1, y1, x2, y2);
        });
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        drawing.add(area -> {
            area.setColor(convertColor(color));
            area.drawRect(x, y, width, height);
        });
    }

    @Override
    public void drawEllipse(int x, int y, int width, int height, CustomColor color) {
        Objects.requireNonNull(color);
        drawing.add(area -> {
            area.setColor(convertColor(color));
            area.drawOval(x, y, width, height);
        });
    }

    @Override
    public void waitForMouseEvents(MouseAdapter mouseCb) {
        graphics.waitForMouseEvents(mouseCb::mouseClicked);
    }

    @Override
    public void refresh() {
        graphics.render(area -> List.copyOf(drawing).forEach(action -> action.accept(area)));
    }
}
