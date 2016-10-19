package com.sample.johns.weatherapp;

import android.content.Context;
import android.location.Location;

import com.sample.johns.weatherapp.main_screen.ForecastPresenter;
import com.sample.johns.weatherapp.main_screen.ForecastView;
import com.sample.johns.weatherapp.pojos.CurrentWeather;
import com.sample.johns.weatherapp.pojos.ForecastResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.mock.Calls;

/**
 * Created by doktor on 10/19/16.
 *
 * The idea with MVP is to test presenters without worrying about the views
 * We just want to check that what we expect to happen (methods from view interface actually get called
 * depending on success or failure) actually happens
 */

@RunWith(JUnit4.class)
public class PresenterTest {
    //could use mockito to mock api

    private WeatherApi api;

    private WeatherApi badApi;

    private ForecastPresenter presenter;

    private ForecastView view;

    private boolean currentWeatherViewCalled = false;

    private boolean failCurrentWeatherCalled = false;

    private boolean fiveDayForecastViewCalled = false;

    private boolean fail5dayForecastCalled = false;

    private Location location = new Location(Context.LOCATION_SERVICE);

    @Before
    public void setUp(){

        location.setLatitude(45.0);

        location.setLongitude(45.0);

        api = new WeatherApi() {
            @Override
            public Call<CurrentWeather> getCurrentWeather(@Query("lat") double lat,
                                                          @Query("lon") double lon,
                                                          @Query("units") String imperial,
                                                          @Query("APPID") String appId) {
                return Calls.response(new CurrentWeather());
            }

            @Override
            public Call<ForecastResponse> get5dayForecast(@Query("lat") double lat,
                                                          @Query("lon") double lon,
                                                          @Query("units") String imperial,
                                                          @Query("APPID") String appId) {
                return Calls.response(new ForecastResponse());
            }
        };

        badApi = new WeatherApi() {
            @Override
            public Call<CurrentWeather> getCurrentWeather(@Query("lat") double lat,
                                                          @Query("lon") double lon,
                                                          @Query("units") String imperial,
                                                          @Query("APPID") String appId) {
                return Calls.failure(new IOException("fail"));
            }

            @Override
            public Call<ForecastResponse> get5dayForecast(@Query("lat") double lat,
                                                          @Query("lon") double lon,
                                                          @Query("units") String imperial,
                                                          @Query("APPID") String appId) {
                return Calls.failure(new IOException("fail"));
            }
        };

        view = new ForecastView() {
            @Override
            public void gotCurrentWeatherSuccess(CurrentWeather currentWeather) {
                currentWeatherViewCalled = true;
            }

            @Override
            public void currentWeatherFailure(String tmsg) {
                failCurrentWeatherCalled = true;
            }

            @Override
            public void got5dayForecast(ForecastResponse forecastResponse) {
                fiveDayForecastViewCalled = true;
            }

            @Override
            public void forecastFailed(String msg) {
                fail5dayForecastCalled = true;
            }
        };
    }

    @Test
    public void testCurrentWeather(){
        presenter = new ForecastPresenter(api);
        presenter.attachView(view);
        presenter.getCurrentWeather(location);
        Assert.assertTrue(currentWeatherViewCalled);
    }

    @Test
    public void testFiveDayForecast(){
        presenter = new ForecastPresenter(api);
        presenter.attachView(view);
        presenter.get5DayForecast(location);
        Assert.assertTrue(fiveDayForecastViewCalled);
    }

    @Test
    public void failCurrentWeather(){
        presenter = new ForecastPresenter(badApi);
        presenter.attachView(view);
        presenter.getCurrentWeather(location);
        Assert.assertTrue(failCurrentWeatherCalled);
    }

    @Test
    public void fail5DayForecast(){
        presenter = new ForecastPresenter(badApi);
        presenter.attachView(view);
        presenter.get5DayForecast(location);
        Assert.assertTrue(fail5dayForecastCalled);
    }
}
