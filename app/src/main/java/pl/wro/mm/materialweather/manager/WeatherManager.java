package pl.wro.mm.materialweather.manager;

import pl.wro.mm.materialweather.network.data.s.weather.Weather;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

    public Observable<Weather> findCityGPS(Double lat, Double lon) {
        return service.findCityByGPS(lat, lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Weather> findCity(String q) {
        return service.findCityByName(q)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface WeatherServiceInterface {
        @GET("/weather")
        Observable<Weather> findCityByGPS(@Query("lat") Double lat, @Query("lon") Double lon);

        @GET("/weather")
        Observable<Weather> findCityByName(@Query("q") String q);
    }
}
