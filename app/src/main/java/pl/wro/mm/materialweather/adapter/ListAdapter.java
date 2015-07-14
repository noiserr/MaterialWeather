package pl.wro.mm.materialweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.wro.mm.materialweather.R;
import pl.wro.mm.materialweather.model.MainWeather;
import pl.wro.mm.materialweather.network.data.photo.weather.Main;

/**
 * Created by noiser on 14.07.15.
 */
public class ListAdapter extends ArrayAdapter<MainWeather> {
    private LayoutInflater mInflater;

    public ListAdapter(Context context, List<MainWeather> objects) {
        super(context, R.layout.weather_cardview, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.weather_cardview, parent, false);
        ImageView weatherIcon = (ImageView) convertView.findViewById(R.id.weather_icon);
        TextView tvCityName = (TextView) convertView.findViewById(R.id.city_name);
        return convertView;
    }


}
