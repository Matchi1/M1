package fr.uge.poo.paint.ex8;

import fr.uge.poo.coolgraphics.CoolGraphics;

public class CoolGraphicsFactory implements CanvasFactory {
    @Override
    public Canvas createCanvas(String title, int width, int height) {
        return new CoolGraphicsAdapter(title, width, height);
    }
}
