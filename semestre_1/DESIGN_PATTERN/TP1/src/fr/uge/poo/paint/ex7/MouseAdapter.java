package fr.uge.poo.paint.ex7;

import fr.uge.poo.coolgraphics.CoolGraphics;
import fr.uge.poo.simplegraphics.SimpleGraphics;

public class MouseAdapter {
    @FunctionalInterface
    public interface MouseCallback extends CoolGraphics.MouseCallback, SimpleGraphics.MouseCallback {
        void mouseClicked(int x, int y);
    }

    public static CoolGraphics.MouseCallback getMouseCallbackCool(MouseCallback mouseCb) {
        CoolGraphics.MouseCallback coolCb = mouseCb;
        return coolCb; 
    }

    public static SimpleGraphics.MouseCallback getMouseCallbackSimple(MouseCallback mouseCb) {
        SimpleGraphics.MouseCallback simpleCb = mouseCb;
        return simpleCb; 
    }
}
