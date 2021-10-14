package fr.uge.poo.paint.ex7;

import java.awt.Color;
import fr.uge.poo.coolgraphics.CoolGraphics.ColorPlus;

public enum CustomColor {
    ORANGE(ColorPlus.ORANGE, Color.ORANGE),
    BLACK(ColorPlus.BLACK, Color.BLACK),
    WHITE(ColorPlus.WHITE, Color.WHITE);

    private final ColorPlus c1;
    private final Color c2;

    CustomColor (ColorPlus c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public ColorPlus getColorPlus() {
        return this.c1;
    }

    public Color getColor() {
        return this.c2;
    }
}

