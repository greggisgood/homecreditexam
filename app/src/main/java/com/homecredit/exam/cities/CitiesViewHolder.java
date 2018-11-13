package com.homecredit.exam.cities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homecredit.exam.MainActivity;
import com.homecredit.exam.R;
import com.homecredit.exam.cities.details.CityDetailsFragment;
import com.homecredit.exam.constants.Values;
import com.homecredit.exam.models.City;
import com.homecredit.exam.models.Coordinates;
import com.homecredit.exam.models.Temperature;
import com.homecredit.exam.models.Weather;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    @BindView(R.id.name) TextView name;
    @BindView(R.id.weather) TextView weather;
    @BindView(R.id.temp) TextView temp;
    @BindView(R.id.coords) TextView coords;
    @BindView(R.id.view) Button btnView;

    public CitiesViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public void setData(City city) {
        if (city != null)
        {
            name.setText(city.getName());

            Temperature temperature = city.getTemperature();
            if (temperature != null)
            {
                temp.setText(String.format(Locale.getDefault(), "%.2f \u2103", temperature.getTemp()));
            }

            List<Weather> weatherList = city.getWeatherList();
            if (!weatherList.isEmpty())
            {
                weather.setText(weatherList.get(0).getMain());
            }

            Coordinates coordinates = city.getCoordinates();
            if (coordinates != null)
            {
                coords.setText(String.format(Locale.getDefault(), "[%s, %s]", String.valueOf(coordinates.getLat()), String.valueOf(coordinates.getLongitude())));
            }

            btnView.setOnClickListener(view -> {
                try
                {
                    MainActivity mainActivity = (MainActivity) context;
                    Gson gson = new Gson();
                    String cityStr = gson.toJson(city);
                    Bundle bundle = new Bundle();
                    bundle.putString(Values.CITY_STR, cityStr);

                    mainActivity.showFragment(CityDetailsFragment.class, true, "", bundle);
                }
                catch (NullPointerException | ClassCastException e)
                {
                    e.printStackTrace();
                }
            });
        }
    }
}
