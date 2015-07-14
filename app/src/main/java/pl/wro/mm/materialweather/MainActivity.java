package pl.wro.mm.materialweather;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.adapter.WeatherAdapter;
import pl.wro.mm.materialweather.event.FindCityEvent;
import pl.wro.mm.materialweather.event.ShowDetailsEvent;
import pl.wro.mm.materialweather.model.MainForecast;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.manager.ForecastManager;
import pl.wro.mm.materialweather.manager.LocationManager;
import pl.wro.mm.materialweather.manager.WeatherManager;
import pl.wro.mm.materialweather.network.data.photo.forecast.Forecast;
import pl.wro.mm.materialweather.network.data.photo.weather.Weather;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.cities_list)
    RecyclerView recyclerView;
    @InjectView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.appbar)
    AppBarLayout appBarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    View promptView;
    FloatingActionButton findCityButton;
    ProgressBar progressBarSearch;
    FloatingActionButton findCityGPSButton;
    ProgressBar progressBarGps;
    ActionBar actionBar;

    List<MainWeather> weatherList = new ArrayList<>();
    WeatherManager weatherManager;
    WeatherAdapter weatherAdapter;
    ForecastManager forecastManager = new ForecastManager();
    AlertDialog alert;
    SwipeableRecyclerViewTouchListener swipeTouchListener;
    LocationManager locationManager = new LocationManager(this);
    EditText findCityET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        setupRecyclerview();
        setupSwipeableTouchListener();
        weatherManager = new WeatherManager();
        recyclerView.addOnItemTouchListener(swipeTouchListener);
        swipeRefreshLayout.setColorSchemeColors(R.attr.colorAccent, R.attr.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("TAGG", "Before: " + new Select().from(MainForecast.class).execute().size() + "");
                        new Delete().from(MainForecast.class).execute();
                        Log.d("TAGG", "After: " + new Select().from(MainForecast.class).execute().size() + "");
                    }
                }, 4000);
            }
        });
        List<MainWeather> mainWeatherList = new Select().from(MainWeather.class).execute();
//

    }


    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
        if (weatherList.isEmpty()) {
            List<MainWeather> mainWeatherList = new Select().from(MainWeather.class).execute();
            weatherList.addAll(mainWeatherList);
            weatherAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    private void setupRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        weatherAdapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(weatherAdapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_friends:
                Log.d("TAG", "friends clicked");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onEvent(FindCityEvent event) {
        progressBarSearch.setVisibility(View.GONE);
        progressBarGps.setVisibility(View.GONE);
        findCityButton.setEnabled(true);
        findCityGPSButton.setEnabled(true);
        if (event.isFound) {
            if (weatherAdapter.isOnCityList(event.weather.getCityName())) {
                Toast.makeText(getApplicationContext(), "City already on list", Toast.LENGTH_SHORT).show();

            } else {
                ForecastManager forecastManager = new ForecastManager();
                forecastManager.getForecast(event.weather.getCityID());
                alert.dismiss();
                weatherList.add(event.weather);
                event.weather.save();
                recyclerView.smoothScrollToPosition(weatherList.size() - 1);
                weatherAdapter.notifyItemInserted(weatherList.size() - 1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Can't find city", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEvent(ShowDetailsEvent event) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("CITY_NAME", event.getCityName());
        intent.putExtra("CITY_ID", event.getCityId());
        intent.putExtra("LAT", event.getLat());
        intent.putExtra("LON", event.getLon());
        startActivity(intent);

    }

    private void removeCity(final int position) {
        final MainWeather weather = weatherAdapter.weatherList.get(position);
        final MainWeather copy = new MainWeather(weather);
        String information;
        weatherAdapter.weatherList.remove(position);
        weatherAdapter.notifyDataSetChanged();
        information = weather.getCityName();
        weather.delete();

        Snackbar.make(recyclerView, information + " removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copy.save();
                        weatherAdapter.weatherList.add(position, copy);
                        weatherAdapter.notifyItemInserted(position);
                    }
                }).show();
    }

    public void addCity(View view) {
        showInputDialog(view);
    }

    private void showInputDialog(final View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        promptView = layoutInflater.inflate(R.layout.findcity_dialog, null);
        findViewsForDialog(promptView);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);
        findCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewNotEmpty()) {
                    showProgressBar(progressBarSearch);
                    weatherManager.findCity(findCityET.getText() + "")
                            .doOnNext(new Action1<Weather>() {
                                @Override
                                public void call(Weather weather) {
                                    hideProgressBar();
                                    findForecastForCity(weather);
                                }
                            })
                            .subscribe();
                } else {
                    Toast.makeText(getApplicationContext(), "Type city name", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Can't get position! Is GPS on?", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert = alertDialogBuilder.create();
        alert.show();

    }

    private void findForecastForCity(Weather weather) {
        if (weather.getCod() != 404){
            processWeather(weather);
            forecastManager.getForecast(weather.getId()).doOnNext(new Action1<Forecast>() {
                @Override
                public void call(Forecast forecast) {
                    forecastManager.parseAndSaveForecast(forecast);
                }
            }).subscribe();
        }else {
            Toast.makeText(getApplicationContext(), "Can't find city", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViewsForDialog(View promptView) {
        findCityET = (EditText) promptView.findViewById(R.id.city_name_edit_text);
        findCityButton = (FloatingActionButton) promptView.findViewById(R.id.find_city_fab);
        findCityGPSButton = (FloatingActionButton) promptView.findViewById(R.id.gps_city_fab);
        progressBarGps = (ProgressBar) promptView.findViewById(R.id.progressBar_gps);
        progressBarSearch = (ProgressBar) promptView.findViewById(R.id.progressBar_search);
    }

    private boolean isTextViewNotEmpty() {
        return !findCityET.getText().toString().equals("");
    }

    private void processWeather(Weather weather) {
        MainWeather mainWeather = parseWeather(weather);
        if (weather.getCod() != 404) {
            if (weatherAdapter.isOnCityList(mainWeather.getCityName())) {
                Toast.makeText(getApplicationContext(), "City already on list", Toast.LENGTH_SHORT).show();
            } else {
                ForecastManager forecastManager = new ForecastManager();
                forecastManager.getForecast(mainWeather.getCityID());
                alert.dismiss();
                weatherList.add(mainWeather);
                mainWeather.save();
                recyclerView.smoothScrollToPosition(weatherList.size() - 1);
                weatherAdapter.notifyItemInserted(weatherList.size() - 1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Can't find city", Toast.LENGTH_SHORT).show();
        }
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


    public void findCityGPS(View view) {
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


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }

    }

    private void setupSwipeableTouchListener() {

        swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipe(int position) {
                        return true;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (final int position : reverseSortedPositions) {
                            removeCity(position);
                        }
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (final int position : reverseSortedPositions) {
                            removeCity(position);
                        }
                    }
                });

    }
}
