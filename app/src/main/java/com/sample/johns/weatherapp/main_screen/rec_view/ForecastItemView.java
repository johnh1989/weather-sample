package com.sample.johns.weatherapp.main_screen.rec_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sample.johns.weatherapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by doktor on 10/18/16.
 */

public class ForecastItemView extends RecyclerView.ViewHolder{

    @BindView(R.id.tv_item_temp)
    TextView itemTemp;

    @BindView(R.id.tv_item_description)
    TextView itemDescription;

    public ForecastItemView(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
