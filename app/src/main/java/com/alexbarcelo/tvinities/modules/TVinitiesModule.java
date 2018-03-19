package com.alexbarcelo.tvinities.modules;

import com.alexbarcelo.tvinities.api.TVinitiesAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Dagger module that provides a singleton instance of TVinitiesAPI
 *
 * @author Alex Barceló
 * @date 16/03/2018
 */

@Module
public class TVinitiesModule {

    @Singleton
    @Provides
    TVinitiesAPI provideTVinitiesAPI(Retrofit retrofit) {
        return new TVinitiesAPI(retrofit);
    }
}
