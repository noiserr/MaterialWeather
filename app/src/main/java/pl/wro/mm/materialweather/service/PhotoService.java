package pl.wro.mm.materialweather.service;

import android.util.Log;

import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.event.PhotoEvent;
import pl.wro.mm.materialweather.photoGson.Photo;
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

    private PhotoInterface service;
    private Weather currentWeather;

    public PhotoService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint("https://api.flickr.com/services/rest")
                .setEndpoint("http://www.panoramio.com/map")
                .build();
        service = restAdapter.create(PhotoInterface.class);


    }

    public void findPhoto(double lat, double lon) {
//        service.findPhoto(cityName, new Callback<Photo>() {
//            @Override
//            public void success(Photo photo, Response response) {
//                Log.d("TAGG", response.getUrl());
////                Photo_ cityPhoto = photo.getPhotos().getPhoto().get(0);
////                String url = "https://farm" + cityPhoto.getFarm() + ".staticflickr.com/" + cityPhoto.getServer() + "/" + cityPhoto.getId() + "_" + cityPhoto.getSecret() + "_z.jpg";
////                Log.d("TAGG", response.getUrl());
//
////                EventBus.getDefault().post(new PhotoEvent(url));
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
// });


        service.findPhoto(lon - 0.1, lat - 0.1, lon + 0.1, lat + 0.1, new Callback<Photo>() {
            @Override
            public void success(Photo photo, Response response) {

                Log.d("TAGG", photo.getPhotos().get(3).getPhotoFileUrl());
                Log.d("TAGG", response.getUrl());
                EventBus.getDefault().post(new PhotoEvent(photo.getPhotos().get(3).getPhotoFileUrl()));

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TAGG", error.getMessage());
                Log.d("TAGG", error.getUrl());
            }
        });


    }


    public interface PhotoInterface {
//        @GET("/?method=flickr.photos.getRecent&api_key="+ "178bdd836bb0f50cc05de0acbd5f918e" +"&format=json" +
//                "&nojsoncallback=1&safe_search=1&per_page=1&page=1&sort=interestingness-desc")
//        void findPhoto(@Query("text") String text, Callback<Photo> cb);

        @GET("/get_panoramas.php?set=public&from=0&to=20&size=original&mapfilter=true")
        void findPhoto(@Query("minx") double minLon, @Query("miny") double minLat,
                       @Query("maxx") double maxLon, @Query("maxy") double maxLat,
                       Callback<Photo> cb);


    }


}
