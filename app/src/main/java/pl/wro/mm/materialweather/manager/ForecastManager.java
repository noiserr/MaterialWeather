package pl.wro.mm.materialweather.manager;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.wro.mm.materialweather.network.data.s.forecast.Forecast;
import pl.wro.mm.materialweather.network.data.s.forecast.ForecastList;
import pl.wro.mm.materialweather.model.MainForecast;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForecastManager {
    private final ForecastServiceInterface service;

    public ForecastManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("units", "metric");
                        request.addQueryParam("cnt", "6");
                    }
                })
                .build();
        service = restAdapter.create(ForecastServiceInterface.class);
    }

    public Observable<Forecast> getForecast(int cityId) {

        return service.getForecast(cityId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void parseAndSaveForecast(Forecast forecast) {

        List<ForecastList> forecastList = forecast.getList();
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
        @GET("/data/2.5/forecast/daily")
        Observable<Forecast> getForecast(@Query("id") int cityID);
    }
}
