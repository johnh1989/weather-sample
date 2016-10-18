package com.sample.johns.weatherapp;


import com.sample.johns.weatherapp.pojos.City;
import com.sample.johns.weatherapp.pojos.CurrentWeather;
import com.sample.johns.weatherapp.pojos.ForecastResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by doktor on 10/18/16.
 */

public interface WeatherApi {

    @GET("weather/")
    Call<CurrentWeather> getCurrentWeather(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("units") String imperial,
                                           @Query("APPID") String appId);

    @GET("forecast/")
    Call<ForecastResponse> get5dayForecast(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("units") String imperial,
                                           @Query("APPID") String appId);
}
