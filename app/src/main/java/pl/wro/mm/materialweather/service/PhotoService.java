package pl.wro.mm.materialweather.service;

import android.util.Log;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.event.FindCityEvent;
import pl.wro.mm.materialweather.image.Photo;
import pl.wro.mm.materialweather.image.Photo_;
import pl.wro.mm.materialweather.weather.Weather;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by noiser on 06.06.15.
 */
public class PhotoService {

    private  PhotoInterface service;
    private Weather currentWeather;
    public PhotoService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.flickr.com/services/rest")
                .build();
         service = restAdapter.create(PhotoInterface.class);




    }

    public void findPhoto(String cityName){
        service.findPhoto(cityName, new Callback<Photo>() {
            @Override
            public void success(Photo photo, Response response) {
                Photo_ cityPhoto = photo.getPhotos().getPhoto().get(0);
                Log.d("PHOTO", "https://farm"+cityPhoto.getFarm()+".staticflickr.com/"+cityPhoto.getServer()+"/"+cityPhoto.getId()+"_"+cityPhoto.getSecret()+"_z.jpg");

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }




    public interface PhotoInterface {
        @GET("/?method=flickr.photos.search&api_key=286e502ffccd2a432ff24d33b2a9d552&format=json" +
                "&nojsoncallback=1&safe_search=1&per_page=1&page=1&sort=interestingness-desc")
        void findPhoto(@Query("text") String text, Callback<Photo> cb);


    }


}
