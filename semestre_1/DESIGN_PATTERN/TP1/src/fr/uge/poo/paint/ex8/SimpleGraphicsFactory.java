package fr.uge.poo.paint.ex8;

public class SimpleGraphicsFactory implements CanvasFactory {
    @Override
    public Canvas createCanvas(String title, int width, int height) {
        return new SimpleGraphicsAdapter(title, width, height);
    }
}
