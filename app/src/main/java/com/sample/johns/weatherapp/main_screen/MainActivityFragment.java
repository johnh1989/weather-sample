package com.sample.johns.weatherapp.main_screen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.sample.johns.weatherapp.App;
import com.sample.johns.weatherapp.main_screen.rec_view.ForecastAdapter;
import com.sample.johns.weatherapp.LocationProvider;
import com.sample.johns.weatherapp.R;
import com.sample.johns.weatherapp.pojos.CurrentWeather;
import com.sample.johns.weatherapp.pojos.ForecastList;
import com.sample.johns.weatherapp.pojos.ForecastResponse;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends MvpFragment<ForecastView, ForecastPresenter>
        implements ForecastView, LocationProvider.GotLocation, SwipeRefreshLayout.OnRefreshListener {

    private Location currentLocation;

    private View mainView;

    private Unbinder unbinder;

    private ForecastAdapter adapter;

    @Inject
    LocationProvider locationProvider;

    @BindView(R.id.tv_city_state)
    TextView tvCityState;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @BindView(R.id.tv_temp)
    TextView tvTemp;

    @BindView(R.id.tv_humidity)
    TextView tvHumidity;

    @BindView(R.id.rec_view)
    RecyclerView recView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    public MainActivityFragment() {
        App.getInjector().inject(this);
    }

    @NonNull
    @Override
    public ForecastPresenter createPresenter() {
        return new ForecastPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set up recycler view
        final int orientation = LinearLayoutManager.VERTICAL;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), orientation, true);
        recView.setLayoutManager(layoutManager);
        adapter = new ForecastAdapter(getActivity());
        recView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationProvider.addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removeListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onGotLocationData(Location location) {
        this.currentLocation = location;
        presenter.getCurrentWeather(location);
        presenter.get5DayForecast(location);
        locationProvider.removeListener(this);
    }

    @Override
    public void gotCurrentWeatherSuccess(CurrentWeather currentWeather) {
        refreshLayout.setRefreshing(false);
        String t = String.valueOf(currentWeather.getMain().getTemp());
        tvTemp.setText(Integer.valueOf(t.split("\\.")[0]) + " \u2109");
        tvCityState.setText(currentWeather.getName() + ", " + currentWeather.getSys().getCountry());
        tvDescription.setText(currentWeather.getWeather().get(0).getDescription());
        tvHumidity.setText(currentWeather.getMain().getHumidity() + "%");
    }

    @Override
    public void currentWeatherFailure(String msg) {
        refreshLayout.setRefreshing(false);
        Snackbar.make(mainView, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void got5dayForecast(ForecastResponse forecastResponse) {
        refreshLayout.setRefreshing(false);
        ArrayList<ForecastList> list = new ArrayList<>();
        list.add(forecastResponse.getList().get(0));
        list.add(forecastResponse.getList().get(1));
        list.add(forecastResponse.getList().get(2));
        list.add(forecastResponse.getList().get(3));
        list.add(forecastResponse.getList().get(4));
        adapter.addForecasts(list);
    }

    @Override
    public void forecastFailed(String msg) {
        refreshLayout.setRefreshing(false);
        Snackbar.make(mainView, msg, Snackbar.LENGTH_LONG).show();
    }

    public void addListener(){
        locationProvider.addListener(this);
    }

    public void removeListener(){
        locationProvider.removeListener(this);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        addListener();
    }
}
