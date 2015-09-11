package pl.wro.mm.materialweather;


import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import pl.wro.mm.materialweather.adapter.WeatherAdapter;

/**
 * Created by noiser on 24.08.15.
 */
public interface MainView {

    void showSearchDialog(AlertDialog dialog);

    void hideSearchDialog(AlertDialog dialog);

    void showToastWithInfo(String info);

    void showSnackBar(Snackbar snackbar);

    void addAdapterToRecyclerView(WeatherAdapter weatherAdapter);
}
