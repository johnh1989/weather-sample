package com.sample.johns.weatherapp;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by doktor on 10/18/16.
 */

public class LocationProvider {

    private static final String TAG = LocationProvider.class.getSimpleName();

    private Application application;

    private ArrayList<GotLocation> listeners = new ArrayList<>();

    private Subscription locationSubscription;

    private final LocationRequest requestFastInterval = new LocationRequest().create()
            .setInterval(1200)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private static LocationProvider instance;

    public interface GotLocation{
        void onGotLocationData(Location location);
    }

    private LocationProvider(Application application){
        this.application = application;
    }

    public static LocationProvider getInstance(Application application){
        if (instance == null){
            instance = new LocationProvider(application);
        }

        return instance;
    }

    public void addListener(GotLocation listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
        if (locationSubscription == null || locationSubscription.isUnsubscribed()){
            startListeningForLocationUpdates();
        }
    }

    public void removeListener(GotLocation listener){
        if (listeners.contains(listener)){
            listeners.remove(listener);
        }
        if(listeners.size() == 0){
            stopListeningForLocationUpdates();
        }
    }

    private void startListeningForLocationUpdates(){
        locationSubscription = new ReactiveLocationProvider(application)
                .getUpdatedLocation(requestFastInterval)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {
                        // infinite stream, this should not get called.
                        // we have to unsubscribe to stop these updates.
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Location location) {
                        Log.d(TAG, "onNext() called with: location = [" + location + "]");
                        if (location != null) {
                            for (GotLocation listener : listeners){
                                listener.onGotLocationData(location);
                            }
                        }
                    }

                });
    }

    private void stopListeningForLocationUpdates(){
        if (locationSubscription != null){
            locationSubscription.unsubscribe();
        }
    }
}

