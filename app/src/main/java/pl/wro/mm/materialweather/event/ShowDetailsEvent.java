package pl.wro.mm.materialweather.event;

/**
 * Created by noiser on 11.06.15.
 */
public class ShowDetailsEvent {

    private String cityName;
    private String cityId;

    public ShowDetailsEvent(String cityName, String cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityId() {
        return cityId;
    }
}
