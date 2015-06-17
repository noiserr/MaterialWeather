package pl.wro.mm.materialweather.service;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.wro.mm.materialweather.forecastGson.Forecast;
import pl.wro.mm.materialweather.forecastGson.ForecastList;
import pl.wro.mm.materialweather.model.MainForecast;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by noiser on 10.06.15.
 */
public class ForecastService {
    private ForecastServiceInterface service;

    public ForecastService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();
        service = restAdapter.create(ForecastServiceInterface.class);


    }

    public void getForecast(int cityId) {

        service.getForecast(cityId, new Callback<Forecast>() {
            @Override
            public void success(Forecast forecast, Response response) {
                Log.d("TAGG", response.getUrl());
                parseAndSaveForecast(forecast);

            }


            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void parseAndSaveForecast(Forecast forecast) {
        java.util.List<ForecastList> forecastList = forecast.getList();
        for (ForecastList oneForecast : forecastList) {

            MainForecast mainForecast = new MainForecast();
            mainForecast.setCityName(forecast.getCity().getName());
            mainForecast.setCityId(forecast.getCity().getId());

            mainForecast.setDate(oneForecast.getDt());
            mainForecast.setConditionId(oneForecast.getWeather().get(0).getId());
            mainForecast.setDayTemperature(oneForecast.getTemp().getDay().intValue() + "");
            mainForecast.setNightTemperature(oneForecast.getTemp().getNight().intValue() + "");
            mainForecast.setDayOfWeek(getDayOfWeek(oneForecast.getDt()));
            mainForecast.setCludiness(oneForecast.getClouds()+"");
            mainForecast.setRainVolume(oneForecast.getRain()+"");
            mainForecast.setWindSpeed(oneForecast.getSpeed()+"");
            mainForecast.setHumidity(oneForecast.getHumidity()+"");
            mainForecast.setPressure(oneForecast.getPressure()+"");
            mainForecast.setDescription(oneForecast.getWeather().get(0).getDescription());
            mainForecast.save();
        }
    }

    private String getDayOfWeek(long dt) {
        Date date = new Date(dt * 1000);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("en"));
        String[] dayNames = symbols.getWeekdays();
        return dayNames[day];
    }


    public interface ForecastServiceInterface {

        @GET("/data/2.5/forecast/daily?units=metric&mode=json&cnt=6")
        void getForecast(@Query("id") int cityID, Callback<Forecast> cb);


    }
}
