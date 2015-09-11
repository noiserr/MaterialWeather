package pl.wro.mm.materialweather.presenter;

import android.content.Context;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import pl.wro.mm.materialweather.MainView;
import pl.wro.mm.materialweather.R;
import pl.wro.mm.materialweather.adapter.WeatherAdapter;
import pl.wro.mm.materialweather.manager.ForecastManager;
import pl.wro.mm.materialweather.manager.LocationManager;
import pl.wro.mm.materialweather.manager.WeatherManager;
import pl.wro.mm.materialweather.model.MainForecast;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.network.data.s.forecast.Forecast;
import pl.wro.mm.materialweather.network.data.s.weather.Weather;
import rx.functions.Action1;

/**
 * Created by noiser on 24.08.15.
 */
public class MainPresenter implements IMainPresenter {

    FloatingActionButton findCityButton;
    EditText cityNameET;
    FloatingActionButton findCityGPSButton;
    ProgressBar progressBarSearch;
    ProgressBar progressBarGps;
    final MainView mainView;
    WeatherAdapter weatherAdapter;
    List<MainWeather> weatherList = new ArrayList<>();
    final WeatherManager weatherManager;
    final ForecastManager forecastManager = new ForecastManager();
    final LocationManager locationManager;
    final Context context;
    AlertDialog dialog;

    public MainPresenter(Context context, MainView mainView) {
        this.mainView = mainView;
        this.context = context;
        weatherManager = new WeatherManager();
        locationManager = new LocationManager(context);
    }


    @Override
    public void showDialog(View view, AlertDialog.Builder alertDialogBuilder) {
        findViewsForDialog(view);
        alertDialogBuilder.setView(view);

        findCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTextViewNotEmpty()) {
                    showProgressBar(progressBarSearch);
                    weatherManager.findCity(cityNameET.getText() + "")
                            .doOnNext(new Action1<Weather>() {
                                @Override
                                public void call(Weather weather) {
                                    hideProgressBar();
                                    processWeather(weather);

                                }
                            })
                            .subscribe();
                } else {
                    mainView.showToastWithInfo(context.getString(R.string.type_city_name));
                }
            }
        });

        findCityGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = locationManager.getLocation();
                showProgressBar(progressBarGps);
                if (location != null) {
                    weatherManager.findCityGPS(location.getLatitude(), location.getLongitude()).doOnNext(new Action1<Weather>() {
                        @Override
                        public void call(Weather weather) {
                            hideProgressBar();
                            processWeather(weather);
                            forecastManager.getForecast(weather.getId()).doOnNext(new Action1<Forecast>() {
                                @Override
                                public void call(Forecast forecast) {
                                    forecastManager.parseAndSaveForecast(forecast);
                                }
                            }).subscribe();
                        }
                    }).subscribe();
                } else {
                    hideProgressBar();
                    mainView.showToastWithInfo(context.getString(R.string.cant_get_gps));
                }
            }
        });
        dialog = alertDialogBuilder.create();
        mainView.showSearchDialog(dialog);
    }

    @Override
    public void onResume() {
        if (weatherList.isEmpty()) {
            weatherList = new Select().from(MainWeather.class).execute();
            weatherAdapter = new WeatherAdapter(weatherList);
            mainView.addAdapterToRecyclerView(weatherAdapter);
        }
    }

    private void processWeather(Weather weather) {
        if (weather.getCod() != 404) {
            MainWeather mainWeather = parseWeather(weather);
            if (weatherAdapter.isOnCityList(weather.getName())) {
                mainView.showToastWithInfo(context.getString(R.string.city_already_on_list));
            } else {
                weatherList.add(mainWeather);
                mainView.hideSearchDialog(dialog);
                mainWeather.save();
                weatherAdapter.notifyItemInserted(weatherList.size() - 1);
            }
        } else {
            mainView.showToastWithInfo(context.getString(R.string.cant_find_city));
        }


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
        return mainWeather;
    }

    private void findViewsForDialog(View promptView) {
        cityNameET = (EditText) promptView.findViewById(R.id.city_name_edit_text);
        findCityButton = (FloatingActionButton) promptView.findViewById(R.id.find_city_fab);
        findCityGPSButton = (FloatingActionButton) promptView.findViewById(R.id.gps_city_fab);
        progressBarGps = (ProgressBar) promptView.findViewById(R.id.progressBar_gps);
        progressBarSearch = (ProgressBar) promptView.findViewById(R.id.progressBar_search);
    }

    private boolean isTextViewNotEmpty() {
        return !cityNameET.getText().toString().equals("");
    }

    private void hideProgressBar() {
        progressBarSearch.setVisibility(View.GONE);
        progressBarGps.setVisibility(View.GONE);
        findCityButton.setEnabled(true);
        findCityGPSButton.setEnabled(true);
    }

    public void showProgressBar(ProgressBar progressBar) {
        findCityButton.setEnabled(false);
        findCityGPSButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void removeCity(final int position, RecyclerView recyclerView) {

        final MainWeather weather = weatherAdapter.weatherList.get(position);
        final MainWeather copy = new MainWeather(weather);
        String information;
        weatherAdapter.weatherList.remove(position);
        weatherAdapter.notifyDataSetChanged();
        information = weather.getCityName();
        removeForecast(weather.getCityID());
        weather.delete();

        Snackbar snackbar = Snackbar.make(recyclerView, information + context.getString(R.string.city_removed), Snackbar.LENGTH_LONG).
                setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        copy.save();
                        weatherAdapter.weatherList.add(position, copy);
                        weatherAdapter.notifyItemInserted(position);
                    }
                });

        mainView.showSnackBar(snackbar);

    }

    private void removeForecast(int cityID) {
        new Delete().from(MainForecast.class).where("city_id=?", cityID + "").execute();
    }

    private void findForecastForCity(Weather weather) {
        if (weather.getCod() != 404) {
            processWeather(weather);
//            forecastManager.getForecast(weather.getId()).doOnNext(new Action1<Forecast>() {
//                @Override
//                public void call(Forecast forecast) {
//                    forecastManager.parseAndSaveForecast(forecast);
//                }
//            }).subscribe();
        } else {
//            Toast.makeText(getApplicationContext(), "Can't find city", Toast.LENGTH_SHORT).show();
        }
    }


}
