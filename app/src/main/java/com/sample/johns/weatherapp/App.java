package com.sample.johns.weatherapp;

import android.app.Application;

import com.sample.johns.weatherapp.dagger.DaggerWeatherComponent;
import com.sample.johns.weatherapp.dagger.Injector;
import com.sample.johns.weatherapp.dagger.NetworkModule;
import com.sample.johns.weatherapp.dagger.WeatherComponent;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by doktor on 10/18/16.
 */

public class App extends Application {

    private static WeatherComponent weatherComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.USE_LEAK_CANARY) {
            LeakCanary.install(this);
        }
        weatherComponent = initialize();
    }

    protected WeatherComponent initialize(){
        return DaggerWeatherComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
    }

    public static Injector getInjector() {
        return Injector.get(weatherComponent);
    }

    /**
     * setter can be used to swap in a mock component when doing espresso tests
     * @param component
     */
    public static void setComponent(WeatherComponent component){
        weatherComponent = component;
    }
}
