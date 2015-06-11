package pl.wro.mm.materialweather.service;

import android.util.Log;

import java.util.ArrayList;

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
                java.util.List<ForecastList> forecastList = forecast.getList();
                Log.d("TAGG", response.getUrl());
                for (ForecastList oneForecast : forecastList) {
                    MainForecast mainForecast = new MainForecast();
                    mainForecast.setDate(oneForecast.getDt());
                    mainForecast.setCityName(forecast.getCity().getName());
                    mainForecast.setConditionId(oneForecast.getWeather().get(0).getId());
                    mainForecast.setMinTemperature(oneForecast.getTemp().getMin() + "");
                    mainForecast.setMaxTemperature(oneForecast.getTemp().getMax() + "");
                    Log.d("TAGG",
                            "Condition: " + mainForecast.getConditionId() +
                                    " Cityname: " + mainForecast.getCityName() +
                                    " CityID: " + mainForecast.getCityId() +
                                    " Date: " + new java.util.Date((long) mainForecast.getDate() * 1000) +
                                    " Min: " + mainForecast.getMinTemperature() +
                                    " Max: " + mainForecast.getMaxTemperature());

                }


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public interface ForecastServiceInterface {

        @GET("/data/2.5/forecast/daily?units=metric&mode=json&cnt=6")
        void getForecast(@Query("id") int cityID, Callback<Forecast> cb);


    }
}
