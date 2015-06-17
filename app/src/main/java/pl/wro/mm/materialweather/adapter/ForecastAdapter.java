package pl.wro.mm.materialweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.wro.mm.materialweather.R;
import pl.wro.mm.materialweather.forecastGson.Forecast;
import pl.wro.mm.materialweather.model.MainForecast;

/**
 * Created by noiser on 17.06.15.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {


    List<MainForecast> forecastList = new ArrayList<>();

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
        holder.presure.setText("Pressure: " + forecast.getPressure()+ " hPa");
        holder.humidity.setText("Humidity: " + forecast.getHumidity() + " %");
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.forecast_title)
        TextView forecastTitle;
        @InjectView(R.id.forecast_description)
        TextView forecastDescription;
        @InjectView(R.id.forecast_day_temp)
        TextView dayTemp;
        @InjectView(R.id.forecast_night_temp)
        TextView nightTemp;
        @InjectView(R.id.forecast_cloudiness)
        TextView cloudiness;
        @InjectView(R.id.forecast_windspeed)
        TextView windSpeed;
        @InjectView(R.id.forecast_pressure)
        TextView presure;
        @InjectView(R.id.forecast_humidity)
        TextView humidity;



        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
