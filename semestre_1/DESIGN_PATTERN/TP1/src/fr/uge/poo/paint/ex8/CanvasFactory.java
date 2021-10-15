package fr.uge.poo.paint.ex8;

import java.awt.*;

public interface CanvasFactory {
    public Canvas createCanvas(String title, int width, int height);
}
