package pl.wro.mm.materialweather.event;

import pl.wro.mm.materialweather.model.MainWeather;

/**
 * Created by noiser on 06.06.15.
 */
public class FindCityEvent {
    public boolean isFound  =false;
    public String cityName;
    public MainWeather weather;


    public FindCityEvent(boolean isFound, String cityName, MainWeather weather) {
        this.isFound = isFound;
        this.cityName = cityName;
        this.weather = weather;
    }
}
