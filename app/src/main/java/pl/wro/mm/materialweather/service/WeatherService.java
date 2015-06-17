package pl.wro.mm.materialweather.service;

import android.util.Log;

import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.event.FindCityEvent;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.weatherGson.Weather;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by noiser on 06.06.15.
 */
public class WeatherService {

    private WeatherServiceInterface service;
    private Weather currentWeather;

    public WeatherService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();
        service = restAdapter.create(WeatherServiceInterface.class);


    }

    public void findCity(final String cityName, String units) {
        service.findCity(cityName, units, new Callback<Weather>() {
            @Override
            public void success(Weather weather, Response response) {
                MainWeather mainWeather = parseWeather(weather);
                if (weather.getCod() == 404) {
                    EventBus.getDefault().post(new FindCityEvent(false, cityName, null));
                } else {
                    EventBus.getDefault().post(new FindCityEvent(true, cityName, mainWeather));

                }
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FindCityEvent(false, cityName, null));
            }
        });


    }

    public void findCityGPS(Double lat, Double lon) {
        service.findCityGPS(lat, lon, new Callback<Weather>() {
            @Override
            public void success(Weather weather, Response response) {
                if (weather.getCod() == 404) {
                    EventBus.getDefault().post(new FindCityEvent(false, null, null));
                } else {
                    String cityName = weather.getName();
                    MainWeather mainWeather = parseWeather(weather);
                    EventBus.getDefault().post(new FindCityEvent(true, cityName, mainWeather));

                }
                Log.d("GPS", weather.getName());
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FindCityEvent(false, null, null));


            }
        });
//        Log.d("GPS", "Jestem TU!");

    }

    public interface WeatherServiceInterface {

        @GET("/data/2.5/weather?units=metric")
        void findCity(@Query("q") String q, @Query("units") String units, Callback<Weather> cb);

        @GET("/data/2.5/weather?units=metric")
        void findCityGPS(@Query("lat") Double lat, @Query("lon") Double lon, Callback<Weather> cb);


    }

    MainWeather parseWeather(Weather weather) {
        MainWeather mainWeather = new MainWeather();
        mainWeather.setCityName(weather.getName());
        mainWeather.setTemp(weather.getMain().getTemp().intValue() + " °C");
        mainWeather.setConditionID(weather.getWeather().get(0).getId());
        mainWeather.setDescription(weather.getWeather().get(0).getMain());
        mainWeather.setPressure(weather.getMain().getPressure() + " hPa");
        mainWeather.setCityID(weather.getId());
        mainWeather.setLat(weather.getCoord().getLat());
        mainWeather.setLon(weather.getCoord().getLon());
        Log.d("TAGG", "lat: " + mainWeather.getLat() + ", lon: " + mainWeather.getLon());
        return mainWeather;
    }


}
