package pl.wro.mm.materialweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import de.greenrobot.event.EventBus;
import pl.wro.mm.materialweather.R;
import pl.wro.mm.materialweather.event.ShowDetailsEvent;
import pl.wro.mm.materialweather.model.MainWeather;

/**
 * Created by noiser on 06.06.15.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    public List<MainWeather> weatherList;


    public WeatherAdapter(List<MainWeather> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_cardview, parent, false);
        return new WeatherViewHolder(viewItem);

    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        MainWeather weather = weatherList.get(position);
        holder.cityName.setText(weather.getCityName());
        holder.currentTemperature.setText(weather.getTemp());
        holder.pressure.setText(weather.getPressure());
        holder.description.setText(weather.getDescription());
//        Log.d("XXXX", weatherID + "");
        holder.weatherIcon.setImageResource(choosIcon(weather.getConditionID()));

    }

    int choosIcon(int weatherID){
        int imageResId = 0;
        if (weatherID / 100 == 8) {
            switch (weatherID) {
                case 800:
                    imageResId = R.drawable.ic_weather_sunny_grey600_48dp;
                    break;
                case 801:
                    imageResId = R.drawable.ic_weather_partlycloudy_grey600_48dp;
                    break;
                case 802:
                    imageResId = R.drawable.ic_weather_cloudy_grey600_48dp;
                    break;
                case 803:
                case 804:
                    imageResId = R.drawable.ic_weather_sunny_grey600_48dp;
                    break;
            }
        } else {
            weatherID = weatherID / 100;
            switch (weatherID) {
                case 2:
                    imageResId = R.drawable.ic_weather_lightning_grey600_48dp;
                    break;
                case 3:
                    imageResId = R.drawable.ic_weather_pouring_grey600_48dp;
                    break;
                case 5:
                    imageResId = R.drawable.ic_weather_rainy_grey600_48dp;
                    break;
                case 6:
                    imageResId = R.drawable.ic_weather_hail_grey600_48dp;
                    break;
                case 7:
                    imageResId = R.drawable.ic_weather_fog_grey600_48dp;
                    break;
            }
        }
        return imageResId;
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public boolean isOnCityList(String name) {
        boolean result = false;
        for (MainWeather mainWeather : weatherList) {
            if (mainWeather.getCityName().equals(name)) {
                result = true;
            }
        }
        return result;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.pressure)
        TextView pressure;
        @Bind(R.id.temp_main)
        TextView currentTemperature;
        @Bind(R.id.city_name)
        TextView cityName;
        @Bind(R.id.weather_description)
        TextView description;
        @Bind(R.id.weather_icon)
        ImageView weatherIcon;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAGG", weatherList.get(getAdapterPosition()).getCityID()+"");
            MainWeather mainWeather = weatherList.get(getAdapterPosition());
            ShowDetailsEvent event = new ShowDetailsEvent(mainWeather.getCityName(), mainWeather.getCityID()+ "",
                    mainWeather.getLon(), mainWeather.getLat());
            EventBus.getDefault().post(event);


        }
    }

}
