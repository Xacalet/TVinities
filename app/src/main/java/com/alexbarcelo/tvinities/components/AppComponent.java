package com.alexbarcelo.tvinities.components;

import com.alexbarcelo.tvinities.modules.AppModule;
import com.alexbarcelo.tvinities.modules.TVinitiesModule;
import com.alexbarcelo.tvinities.activities.DetailActivity;
import com.alexbarcelo.tvinities.activities.ListActivity;
import com.alexbarcelo.tvinities.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component that injects the module dependencies on each activity
 *
 * @author Alex Barceló
 * @date 14/03/2018
 */

@Singleton
@Component(modules = { AppModule.class, NetworkModule.class, TVinitiesModule.class })
public interface AppComponent {

    void inject(ListActivity listActivity);

    void inject(DetailActivity detailActivity);
}