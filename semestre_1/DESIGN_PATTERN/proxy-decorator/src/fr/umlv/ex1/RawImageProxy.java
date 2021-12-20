package fr.umlv.ex1;

import com.evilcorp.imagine.RawImage;

public class RawImageProxy implements Image {
    private final String url;
    private RawImage rawImage;

    public RawImageProxy(String url) {
        this.url = url;
    }

    private void initialize() {
        if(rawImage == null) {
            rawImage = RawImage.download(this.url);
        }
    }

    public String name() {
        initialize();
        return rawImage.name();
    }

    public int size() {
        initialize();
        return rawImage.size();
    }

    public double hue() {
        initialize();
        return rawImage.hue();
    }

    static Image downloadLazy(String url) {
        return new RawImageProxy(url);
    }

    @Override
    public boolean equals(Object o) {
        initialize();
        return rawImage.equals(o);
    }

    @Override
    public int hashCode() {
        initialize();
        return rawImage.hashCode();
    }
}
