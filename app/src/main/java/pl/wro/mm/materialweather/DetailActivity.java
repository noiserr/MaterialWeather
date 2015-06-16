package pl.wro.mm.materialweather;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import pl.wro.mm.materialweather.event.PhotoEvent;
import pl.wro.mm.materialweather.model.MainForecast;
import pl.wro.mm.materialweather.service.PhotoService;

public class DetailActivity extends AppCompatActivity {
    @InjectView(R.id.backdrop)
    ImageView imageView;
    @InjectView(R.id.detail_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.main_forecast_icon)
    ImageView mainWeatherIcon;
    @InjectView(R.id.day_one_name)
    TextView dayOneName;
    @InjectView(R.id.day_two_name)
    TextView dayTwoName;
    @InjectView(R.id.day_three_name)
    TextView dayThreeName;
    @InjectView(R.id.day_four_name)
    TextView dayFourName;
    @InjectView(R.id.day_five_name)
    TextView dayFiveName;
    @InjectView(R.id.main_forecast_description)
    TextView mainForecastDescription;
    @InjectView(R.id.main_forecast_day_temp)
    TextView mainForecastDayTemp;
    @InjectView(R.id.main_forecast_night_temp)
    TextView mainForecastNightTemp;
    @InjectView(R.id.main_forecast_cloudiness)
    TextView cloudiness;
    @InjectView(R.id.main_forecast_3h_rain)
    TextView rain;
    @InjectView(R.id.main_forecast_windspeed)
    TextView windSpeed;
    @InjectView(R.id.main_forecast_humidity)
    TextView humidity;
    @InjectView(R.id.main_forecast_pressure)
    TextView pressure;


    List<TextView> textViews = new ArrayList<>();


    String cityID;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PhotoService photoService = new PhotoService();
        Intent intent = getIntent();
        cityName = intent.getStringExtra("CITY_NAME");
        cityID = intent.getStringExtra("CITY_ID");
        photoService.findPhoto(cityName);
        collapsingToolbar.setTitle(cityName);
        textViews.add(dayOneName);
        textViews.add(dayTwoName);
        textViews.add(dayThreeName);
        textViews.add(dayFourName);
        textViews.add(dayFiveName);
        changeForecast();


    }

    private void changeForecast() {
        List<MainForecast> forecastList = new Select().from(MainForecast.class).where("city_id=?", cityID + "").execute();
        MainForecast mainForecast = forecastList.get(0);
        mainForecastDayTemp.setText(mainForecast.getDayTemperature());
//        textViews.get(0).setText("H");
        for (int i = 1; i < forecastList.size(); i++) {
            textViews.get(i - 1).setText(forecastList.get(i).getDayOfWeek());
//            textViews.get(0).setText(forecastList.get(1).getDayOfWeek());
//            textViews.get(1).setText(forecastList.get(2).getDayOfWeek());

        }

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
        Picasso.with(this)
                .load(event.getUrl())
                .into(imageView);
    }
}
