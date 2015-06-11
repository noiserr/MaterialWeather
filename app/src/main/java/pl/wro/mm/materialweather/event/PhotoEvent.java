package pl.wro.mm.materialweather.event;

/**
 * Created by noiser on 11.06.15.
 */
public class PhotoEvent {

    String url;

    public PhotoEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
