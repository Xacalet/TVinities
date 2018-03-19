package com.alexbarcelo.tvinities.app;

import android.app.Application;

import com.alexbarcelo.tvinities.components.AppComponent;
import com.alexbarcelo.tvinities.modules.AppModule;
import com.alexbarcelo.tvinities.components.DaggerAppComponent;
import com.alexbarcelo.tvinities.modules.TVinitiesModule;
import com.alexbarcelo.tvinities.modules.NetworkModule;

/**
 * Custom application that loads the component with all the required modules
 *
 * @author Alex Barceló
 * @date 14/03/2018
 */

public class CustomApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("https://api.themoviedb.org/"))
                .tVinitiesModule(new TVinitiesModule())
                .build();
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
