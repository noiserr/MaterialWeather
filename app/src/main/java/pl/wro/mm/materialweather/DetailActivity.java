package pl.wro.mm.materialweather;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.adapter.ForecastAdapter;
import pl.wro.mm.materialweather.event.PhotoEvent;
import pl.wro.mm.materialweather.manager.ForecastManager;
import pl.wro.mm.materialweather.model.MainForecast;
import pl.wro.mm.materialweather.network.data.s.forecast.Forecast;
import rx.functions.Action1;

public class DetailActivity extends AppCompatActivity {
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.forecast_recyclerview) RecyclerView recyclerView;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;
    ForecastAdapter forecastAdapter;

    List<TextView> textViews = new ArrayList<>();
    String cityID;
    String cityName;
    double lon;
    double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getExtra();

        List<MainForecast> forecastList = new Select().from(MainForecast.class).where("city_id=?", cityID + "").execute();
        if(forecastList.isEmpty()) {
            final ForecastManager forecastManager = new ForecastManager();
            forecastManager.getForecast(Integer.valueOf(cityID)).doOnNext(new Action1<Forecast>() {
                @Override
                public void call(Forecast forecast) {

                    Toast.makeText(getApplicationContext(), "IN RX", Toast.LENGTH_SHORT).show();

                    forecastManager.parseAndSaveForecast(forecast);
                    List<MainForecast> forecastList = new Select().from(MainForecast.class).where("city_id=?", cityID + "").execute();
                    setUpRecyclerView(forecastList);

                }
            }).doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();

                }
            }).subscribe();
        }else{
            setUpRecyclerView(forecastList);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView(List<MainForecast> forecastList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ForecastAdapter forecastAdapter = new ForecastAdapter(forecastList);
        recyclerView.setAdapter(forecastAdapter);
    }

    public void getExtra() {
        Bundle bundle= getIntent().getExtras();
        cityName = bundle.getString("CITY_NAME");
        cityID = bundle.getString("CITY_ID");
        lon = bundle.getDouble("LON");
        lat = bundle.getDouble("LAT");
    }


}
