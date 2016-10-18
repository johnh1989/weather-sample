package com.sample.johns.weatherapp.dagger;

import com.sample.johns.weatherapp.main_screen.ForecastPresenter;
import com.sample.johns.weatherapp.main_screen.MainActivity;
import com.sample.johns.weatherapp.main_screen.MainActivityFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by doktor on 10/18/16.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface WeatherComponent {
    void inject(MainActivity mainActivity);
    void inject(MainActivityFragment mainActivityFragment);
    void inject(ForecastPresenter forecastPresenter);
}
