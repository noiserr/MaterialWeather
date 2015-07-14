package pl.wro.mm.materialweather.manager;

import android.util.Log;

import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.event.FindCityEvent;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.network.data.photo.weather.Weather;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WeatherManager {

    private WeatherServiceInterface service;

    public WeatherManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org/data/2.5")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("units", "metric");
                    }
                })
                .build();
        service = restAdapter.create(WeatherServiceInterface.class);
    }

    public Observable<Weather> findCity(String q) {
        return service.findCity(q)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Weather> findCityGPS(Double lat, Double lon) {
        return service.findCityGPS(lat, lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface WeatherServiceInterface {
        @GET("/weather")
        Observable<Weather> findCityGPS(@Query("lat") Double lat, @Query("lon") Double lon);
        @GET("/weather")
        Observable<Weather> findCity(@Query("q") String q);
    }
}
