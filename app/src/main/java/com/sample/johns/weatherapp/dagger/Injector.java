package com.sample.johns.weatherapp.dagger;

import com.sample.johns.weatherapp.main_screen.ForecastPresenter;
import com.sample.johns.weatherapp.main_screen.MainActivity;
import com.sample.johns.weatherapp.main_screen.MainActivityFragment;

/**
 * Created by doktor on 10/18/16.
 */

public class Injector {

    private static Injector instance;

    private WeatherComponent mainComponent;

    public Injector(WeatherComponent mainComponent){
        this.mainComponent = mainComponent;
    }

    public static Injector get(WeatherComponent mainComponent){
        if (instance == null){
            instance = new Injector(mainComponent);
        }

        return instance;
    }

    public void inject(MainActivity mainActivity){
        mainComponent.inject(mainActivity);
    }

    public void inject(MainActivityFragment mainActivityFragment){
        mainComponent.inject(mainActivityFragment);
    }

    public void inject(ForecastPresenter forecastPresenter){
        mainComponent.inject(forecastPresenter);
    }
}
