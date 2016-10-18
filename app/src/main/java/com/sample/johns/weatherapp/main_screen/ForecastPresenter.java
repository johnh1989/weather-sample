package com.sample.johns.weatherapp.main_screen;

import android.location.Location;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.sample.johns.weatherapp.App;
import com.sample.johns.weatherapp.WeatherApi;
import com.sample.johns.weatherapp.pojos.CurrentWeather;
import com.sample.johns.weatherapp.pojos.ForecastResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by doktor on 10/18/16.
 */

public class ForecastPresenter extends MvpBasePresenter<ForecastView> {

    private static final String TAG = ForecastPresenter.class.getSimpleName();

    private static final String API_KEY = "7e4d8479deac715bb7cefb6821322ac3";

    @Inject
    WeatherApi api;

    public ForecastPresenter(){
        App.getInjector().inject(this);
    }

    public void getCurrentWeather(Location location){
        api.getCurrentWeather(location.getLatitude(), location.getLongitude(), "imperial", API_KEY)
                .enqueue(new Callback<CurrentWeather>() {
                    @Override
                    public void onResponse(Call<CurrentWeather> call,
                                           Response<CurrentWeather> response) {
                        if (response.isSuccessful()){
                            CurrentWeather currentWeather = response.body();
                            Log.d(TAG, currentWeather.toString());
                            if (isViewAttached()){
                                getView().gotCurrentWeatherSuccess(currentWeather);
                            }
                        } else {
                            if (isViewAttached()){
                                getView().currentWeatherFailure("failed to load current weather data");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        if (isViewAttached()){
                            getView().currentWeatherFailure(t.getMessage());
                        }
                    }
                });
    }

    public void get5DayForecast(Location location){
        api.get5dayForecast(location.getLatitude(), location.getLongitude(), "imperial", API_KEY)
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                        if (response.isSuccessful()){
                            ForecastResponse forecastResponse = response.body();
                            if (isViewAttached()){
                                getView().got5dayForecast(forecastResponse);
                            }
                        } else {
                            if (isViewAttached()){
                                getView().forecastFailed("failed to get 5 day forecast");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
                        if(isViewAttached()){
                            Log.e(TAG, "onFailure: ", t);
                            getView().forecastFailed(t.getMessage());
                        }
                    }
                });
    }
}
