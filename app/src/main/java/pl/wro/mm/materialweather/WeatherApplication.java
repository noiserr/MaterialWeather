package pl.wro.mm.materialweather;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.stetho.Stetho;

/**
 * Created by noiser on 24.08.15.
 */
public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
