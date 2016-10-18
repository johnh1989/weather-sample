package com.sample.johns.weatherapp.main_screen.rec_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sample.johns.weatherapp.R;
import com.sample.johns.weatherapp.pojos.ForecastList;

import java.util.ArrayList;

/**
 * Created by doktor on 10/18/16.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastItemView> {

    private ArrayList<ForecastList> forecast = new ArrayList<>();

    private Context context;

    public ForecastAdapter(Context context){this.context = context;}

    @Override
    public ForecastItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForecastItemView(LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ForecastItemView holder, int position) {
        ForecastList list = forecast.get(position);
        String t = String.valueOf(list.getMain().getTemp());
        holder.itemTemp.setText((Integer.valueOf(t.split("\\.")[0]) + " \u2109"));
        holder.itemDescription.setText(list.getWeather().get(0).getDescription());
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }

    public void addForecasts(ArrayList<ForecastList> items){
        forecast.clear();
        forecast.addAll(items);
        notifyDataSetChanged();
    }
}
