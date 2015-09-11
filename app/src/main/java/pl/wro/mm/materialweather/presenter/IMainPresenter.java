package pl.wro.mm.materialweather.presenter;

import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by noiser on 24.08.15.
 */
public interface IMainPresenter {

    void showDialog(View view, AlertDialog.Builder builder);

    void onResume();

}
