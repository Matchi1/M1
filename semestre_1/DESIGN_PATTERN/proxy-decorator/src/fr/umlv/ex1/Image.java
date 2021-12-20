package fr.umlv.ex1;

public interface Image {
    static Image download(String url) {
        return RawImageProxy.downloadLazy(url);
    }
    String name();
    int size();
    double hue();
}
