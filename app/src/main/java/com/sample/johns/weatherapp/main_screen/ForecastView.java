package com.sample.johns.weatherapp.main_screen;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.sample.johns.weatherapp.pojos.CurrentWeather;
import com.sample.johns.weatherapp.pojos.ForecastResponse;

/**
 * Created by doktor on 10/18/16.
 */

public interface ForecastView extends MvpView {

    void gotCurrentWeatherSuccess(CurrentWeather currentWeather);

    void currentWeatherFailure(String tmsg);

    void got5dayForecast(ForecastResponse forecastResponse);

    void forecastFailed(String msg);
}
