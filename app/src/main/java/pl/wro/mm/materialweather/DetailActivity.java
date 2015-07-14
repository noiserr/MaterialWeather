package pl.wro.mm.materialweather;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.adapter.ForecastAdapter;
import pl.wro.mm.materialweather.event.PhotoEvent;
import pl.wro.mm.materialweather.model.MainForecast;
import pl.wro.mm.materialweather.manager.PhotoManager;

public class DetailActivity extends AppCompatActivity {
    @InjectView(R.id.forecast_recyclerview)
    RecyclerView recyclerView;
    @InjectView(R.id.backdrop)
    ImageView imageView;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.detail_toolbar)
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
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getExtra();

        List<MainForecast> forecastList = new Select().from(MainForecast.class).where("city_id=?", cityID + "").execute();
        PhotoManager photoManager = new PhotoManager();
        photoManager.findPhoto(lat, lon);
        Log.d("TAGG", "forecastList.size: "+ forecastList.size());


        collapsingToolbar.setTitle(cityName);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
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

    public void onEvent(PhotoEvent event) {
        Log.wtf("XXX", event.getUrl());
        Picasso.with(this)
                .load(event.getUrl())
                .into(imageView);
    }
}
