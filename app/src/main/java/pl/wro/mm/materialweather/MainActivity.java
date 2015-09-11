package pl.wro.mm.materialweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.adapter.WeatherAdapter;
import pl.wro.mm.materialweather.event.ShowDetailsEvent;
import pl.wro.mm.materialweather.manager.ForecastManager;
import pl.wro.mm.materialweather.manager.LocationManager;
import pl.wro.mm.materialweather.manager.WeatherManager;
import pl.wro.mm.materialweather.model.MainForecast;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.network.data.s.forecast.Forecast;
import pl.wro.mm.materialweather.network.data.s.weather.Weather;
import pl.wro.mm.materialweather.presenter.MainPresenter;
import rx.Observable;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, MainView {
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.cities_list)
    RecyclerView recyclerView;
    @Bind(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    View promptView;

    ActionBar actionBar;

    SwipeableRecyclerViewTouchListener swipeTouchListener;
    MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mainPresenter = new MainPresenter(this, this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
//        setupRecyclerview();
        setupSwipeableTouchListener();
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
//        List<MainWeather> mainWeatherList = new Select().from(MainWeather.class).execute();

    }

    public Observable<List<MainWeather>> getRXList() {
        List<MainWeather> mainWeatherList = new Select().from(MainWeather.class).execute();
        return Observable.just(mainWeatherList);
    }


    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
        setupRecyclerview();
        mainPresenter.onResume();
//
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    private void setupRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        weatherAdapter = new WeatherAdapter(weatherList);
//
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



    public void onEvent(ShowDetailsEvent event) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("CITY_NAME", event.getCityName());
        intent.putExtra("CITY_ID", event.getCityId());
        intent.putExtra("LAT", event.getLat());
        intent.putExtra("LON", event.getLon());
        startActivity(intent);

    }



    @OnClick(R.id.fab)
    public void addCity(View view) {
        View promptView = getLayoutInflater().inflate(R.layout.findcity_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        mainPresenter.showDialog(promptView, alertDialogBuilder);
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
                            mainPresenter.removeCity(position, recyclerView);
                        }
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (final int position : reverseSortedPositions) {
                            mainPresenter.removeCity(position, recyclerView);
                        }
                    }
                });

    }

    @Override
    public void showSearchDialog(AlertDialog dialog) {
        dialog.show();

    }

    @Override
    public void hideSearchDialog(AlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void showToastWithInfo(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackBar(Snackbar snackbar) {
        snackbar.show();
    }

    @Override
    public void addAdapterToRecyclerView(WeatherAdapter weatherAdapter) {
        recyclerView.setAdapter(weatherAdapter);
    }
}
