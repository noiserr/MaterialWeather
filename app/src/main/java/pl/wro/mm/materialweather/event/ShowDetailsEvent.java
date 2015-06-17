package pl.wro.mm.materialweather.event;

/**
 * Created by noiser on 11.06.15.
 */
public class ShowDetailsEvent {

    private String cityName;
    private String cityId;
    private double lon;
    private double lat;

    public ShowDetailsEvent(String cityName, String cityId, double lon, double lat) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.lon = lon;
        this.lat = lat;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
