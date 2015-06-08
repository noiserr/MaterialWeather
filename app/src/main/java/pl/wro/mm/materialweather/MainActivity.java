package pl.wro.mm.materialweather;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.adapter.WeatherAdapter;
import pl.wro.mm.materialweather.event.FindCityEvent;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.service.LocationService;
import pl.wro.mm.materialweather.service.PhotoService;
import pl.wro.mm.materialweather.service.WeatherService;
import pl.wro.mm.materialweather.weather.Weather;


public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.cities_list)
    RecyclerView recyclerView;

    ActionBar actionBar;
    List<MainWeather> weatherList = new ArrayList<>();
    WeatherService weatherService;
    WeatherAdapter weatherAdapter;
    AlertDialog alert;

    LocationService locationService = new LocationService(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        setupRecyclerview();
        weatherService = new WeatherService();

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
//                                for (int position : reverseSortedPositions) {
//                                    weatherList.remove(position);
//                                    weatherAdapter.notifyItemRemoved(position);
//                                }
//                                weatherAdapter.notifyDataSetChanged();
                                Log.d("SWIPE", "LEFT");
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    weatherList.get(position).delete();
                                    weatherList.remove(position);
                                    weatherAdapter.notifyItemRemoved(position);
                                }
//                                weatherAdapter.notifyDataSetChanged();
                                Log.d("SWIPE", "RIGHT");

                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);


//        ImageView imageView = (ImageView) findViewById(R.id.web_image);


//
//        MainWeather mainWeather = new MainWeather();
//        mainWeather.setCityName("GUNOW");
//        mainWeather.setPressure("1000 hPa");
//        mainWeather.setTemp("45");
//        mainWeather.setDescription("jebie guwnem");
//
//        mainWeather.save();

        List<MainWeather> mainWeatherList = new Select().from(MainWeather.class).execute();
//

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "REASUMUJĄĆ");
        List<MainWeather> mainWeatherList = new Select().from(MainWeather.class).execute();
        weatherList.addAll(mainWeatherList);
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
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onEvent(FindCityEvent event) {
        if (event.isFound) {
            alert.dismiss();
            weatherList.add(event.weather);
            event.weather.save();
            recyclerView.smoothScrollToPosition(weatherList.size() - 1);
            weatherAdapter.notifyItemInserted(weatherList.size() - 1);
        } else {
            Toast.makeText(getApplicationContext(), "Can't find city!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCity(View view) {
        showInputDialog(view);

    }

    private void showInputDialog(final View view) {

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.findcity_dialog, null);
        final EditText findCityET = (EditText) promptView.findViewById(R.id.city_name_edit_text);
        final FloatingActionButton findCityButton = (FloatingActionButton) promptView.findViewById(R.id.find_city_fab);
        final FloatingActionButton findCityGPSButton = (FloatingActionButton) promptView.findViewById(R.id.gps_city_fab);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        findCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherService.findCity(findCityET.getText() + "", "metric");
//                PhotoService photoService = new PhotoService();
//                photoService.findPhoto(findCityTV.getText() + "");

            }
        });


        findCityGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location location = locationService.getLocation();
                if (location != null) {
                    weatherService.findCityGPS(location.getLatitude(), location.getLongitude());

                } else {
                    Toast.makeText(getApplicationContext(), "Can't get position!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        alert = alertDialogBuilder.create();
        alert.show();

    }

    public void findCityGPS(View view) {
    }


}
