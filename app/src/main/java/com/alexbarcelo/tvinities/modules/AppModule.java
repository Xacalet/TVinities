package com.alexbarcelo.tvinities.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides Application context to the rest of modules
 *
 * @author Alex Barceló
 */

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application application() {
        return mApplication;
    }
}
