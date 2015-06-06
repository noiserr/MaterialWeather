package pl.wro.mm.materialweather;

import pl.wro.mm.materialweather.weather.Weather;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by noiser on 05.06.15.
 */
public interface WeatherService {
    @GET("/data/2.5/weather")
    void getWeather(@Query("q") String q, Callback<Weather> cb);
}
