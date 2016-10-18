package com.sample.johns.weatherapp.dagger;

import android.app.Application;

import com.google.gson.Gson;
import com.sample.johns.weatherapp.LocationProvider;
import com.sample.johns.weatherapp.WeatherApi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by doktor on 10/18/16.
 */

@Module
public class NetworkModule {

    private final static String baseUrl = "http://api.openweathermap.org/data/2.5/";

    private final Application application;

    public NetworkModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }

    @Provides
    @Singleton
    Cache providesOkHttpCache(final Application application) {
        final int cacheSize = 12 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient providesStethoOkHttpClient(final Cache cache) {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    WeatherApi providesWeatherApi(final OkHttpClient client){


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .baseUrl(baseUrl)
                .client(client)
                .build();

        return retrofit.create(WeatherApi.class);
    }

    @Provides
    @Singleton
    LocationProvider providesLocationProvider(final Application application){
        return LocationProvider.getInstance(application);
    }
}
