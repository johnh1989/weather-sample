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

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.mock.Calls;

/**
 * Created by doktor on 10/19/16.
 */

@RunWith(JUnit4.class)
public class PresenterTest {
    //could use mockito to mock api
    //also would want to test the fail case which can be mocked the same way here

    private WeatherApi api;

    private ForecastPresenter presenter;

    private ForecastView view;

    private boolean currentWeatherViewCalled = false;

    private boolean fiveDayForecastViewCalled = false;

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

        view = new ForecastView() {
            @Override
            public void gotCurrentWeatherSuccess(CurrentWeather currentWeather) {
                currentWeatherViewCalled = true;
            }

            @Override
            public void currentWeatherFailure(String tmsg) {
                //this should be tested also
            }

            @Override
            public void got5dayForecast(ForecastResponse forecastResponse) {
                fiveDayForecastViewCalled = true;
            }

            @Override
            public void forecastFailed(String msg) {
                //this should be tested also
            }
        };

        presenter = new ForecastPresenter(api);
        presenter.attachView(view);
    }

    @Test
    public void testCurrentWeather(){
        presenter.getCurrentWeather(location);
        Assert.assertTrue(currentWeatherViewCalled);
    }

    @Test
    public void testFiveDayForecast(){
        presenter.get5DayForecast(location);
        Assert.assertTrue(fiveDayForecastViewCalled);
    }
}
