package pl.wro.mm.materialweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.wro.mm.materialweather.R;
import pl.wro.mm.materialweather.model.MainForecast;

/**
 * Created by noiser on 17.06.15.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<MainForecast> forecastList = new ArrayList<>();

    public ForecastAdapter(List<MainForecast> forecastList) {
        this.forecastList = forecastList;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_cardview, parent, false);

        return new ForecastViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        MainForecast forecast = forecastList.get(position);
        String desctiption = forecast.getDescription();
        desctiption = Character.toString(desctiption.charAt(0)).toUpperCase()+desctiption.substring(1);
        holder.forecastTitle.setText(forecast.getDayOfWeek());
        holder.forecastDescription.setText(desctiption);
        holder.dayTemp.setText("Day: " + forecast.getDayTemperature() + " °C");
        holder.nightTemp.setText("Night: " + forecast.getNightTemperature() + " °C");
        holder.cloudiness.setText("Cloudines: " + forecast.getCludiness()+ " %");
        holder.windSpeed.setText("Wind speed: " + forecast.getWindSpeed() + "m/s");
        holder.pressure.setText("Pressure: " + forecast.getPressure() + " hPa");
        holder.humidity.setText("Humidity: " + forecast.getHumidity() + " %");
        holder.icon.setImageResource(chooseIcon(forecast.getConditionId()));
    }

    private int chooseIcon(int conditionId) {
        int imageResId = 0;
        if (conditionId / 100 == 8) {
            switch (conditionId) {
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
            conditionId = conditionId / 100;
            switch (conditionId) {
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
        return forecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.forecast_title)
        TextView forecastTitle;
        @Bind(R.id.forecast_description)
        TextView forecastDescription;
        @Bind(R.id.forecast_day_temp)
        TextView dayTemp;
        @Bind(R.id.forecast_night_temp)
        TextView nightTemp;
        @Bind(R.id.forecast_cloudiness)
        TextView cloudiness;
        @Bind(R.id.forecast_windspeed)
        TextView windSpeed;
        @Bind(R.id.forecast_pressure)
        TextView pressure;
        @Bind(R.id.forecast_humidity)
        TextView humidity;
        @Bind(R.id.forecast_icon)
        ImageView icon;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
