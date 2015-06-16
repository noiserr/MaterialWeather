package pl.wro.mm.materialweather.service;

import android.util.Log;

import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.R;
import pl.wro.mm.materialweather.event.PhotoEvent;
import pl.wro.mm.materialweather.image.Photo;
import pl.wro.mm.materialweather.image.Photo_;
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
            String gett;
            @Override
            public void success(Photo photo, Response response) {
//                Log.d("TAGG", response.getUrl());
                Photo_ cityPhoto = photo.getPhotos().getPhoto().get(0);
                String url = "https://farm" + cityPhoto.getFarm() + ".staticflickr.com/" + cityPhoto.getServer() + "/" + cityPhoto.getId() + "_" + cityPhoto.getSecret() + "_z.jpg";

                EventBus.getDefault().post(new PhotoEvent(url));
            }

            @Override
            public void failure(RetrofitError error) {

            }

        });


    }




    public interface PhotoInterface {
        @GET("/?method=flickr.photos.search&api_key="+ "178bdd836bb0f50cc05de0acbd5f918e" +"&format=json" +
                "&nojsoncallback=1&safe_search=1&per_page=1&page=1&sort=interestingness-desc")
        void findPhoto(@Query("text") String text, Callback<Photo> cb);


    }


}
