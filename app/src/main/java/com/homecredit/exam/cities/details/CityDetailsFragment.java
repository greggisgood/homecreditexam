package com.homecredit.exam.cities.details;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homecredit.exam.MainActivity;
import com.homecredit.exam.R;
import com.homecredit.exam.api.ApiCallback;
import com.homecredit.exam.api.calls.CityCall;
import com.homecredit.exam.constants.Values;
import com.homecredit.exam.models.City;
import com.homecredit.exam.models.Temperature;
import com.homecredit.exam.models.Weather;
import com.homecredit.exam.utils.AppLogger;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityDetailsFragment extends Fragment {
    private static final String TAG = CityDetailsFragment.class.getSimpleName();
    private Unbinder unbinder;
    private CityCall cityCall;

    @BindView(R.id.name) TextView txtName;
    @BindView(R.id.image) ImageView imgCity;
    @BindView(R.id.temperature) TextView txtTemp;
    @BindView(R.id.pressure) TextView txtPressure;
    @BindView(R.id.humidity) TextView txtHumidity;
    @BindView(R.id.avg_temp) TextView txtAvgTemp;
    @BindView(R.id.back) ImageButton ibBack;
    @BindView(R.id.refresh) ImageButton ibRefresh;
    @BindView(R.id.title) TextView txtTitle;
    @BindView(R.id.group) Group group;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.error) TextView txtError;

    @BindString(R.string.pressure) String strPressure;
    @BindString(R.string.humidity) String strHumidity;
    @BindString(R.string.avg_temp) String strAvgTemp;
    @BindString(R.string.city_details) String strCityDetails;
    @BindString(R.string.hpa) String strHpa;

    private City mainCity;

    public CityDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            Gson gson = new Gson();
            String cityStr = bundle.getString(Values.CITY_STR);
            mainCity = gson.fromJson(cityStr, City.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupData();
        return view;
    }

    @OnClick(R.id.back)
    public void onBack() {
        try
        {
            if (getContext() != null)
            {
                MainActivity mainActivity = (MainActivity) getContext();
                if (mainActivity != null)
                {
                    mainActivity.popBackStack();
                }
            }
        }
        catch (NullPointerException | ClassCastException e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.refresh)
    public void onRefresh() {
        getCity();
    }

    /**
     * Retrieves the mainCity information
     */
    private void getCity()
    {
        Context ctx = getContext();
        if (ctx != null && mainCity != null)
        {
            // If an existing request is being processed, cancel it
            if (cityCall != null)
            {
                cityCall.cancelCityCallRequest();
            }
            else
            {
                cityCall = CityCall.with(ctx);
            }

            progressBar.setVisibility(View.VISIBLE);
            txtError.setVisibility(View.GONE);
            group.setVisibility(View.GONE);

            cityCall.getCity(String.valueOf(mainCity.getId()), new ApiCallback.GetCityListener() {
                @Override
                public void onSuccess(City city) {
                    AppLogger.info(TAG, "getCity onSuccess()");

                    progressBar.setVisibility(View.GONE);
                    txtError.setVisibility(View.GONE);
                    group.setVisibility(View.VISIBLE);

                    mainCity = city;
                    setupData();
                }

                @Override
                public void onError() {
                    AppLogger.error(TAG, "getCity onError()");

                    progressBar.setVisibility(View.GONE);
                    txtError.setVisibility(View.VISIBLE);
                    group.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setupData() {
        txtTitle.setText(strCityDetails);
        if (mainCity != null) {
            txtName.setText(mainCity.getName());

            Temperature temperature = mainCity.getTemperature();
            Weather weather = mainCity.getWeatherList().get(0);

            if (weather != null && StringUtils.isNotBlank(weather.getIcon()))
            {
                Picasso.get()
                        .load("http://openweathermap.org/img/w/" + weather.getIcon() + ".png")
                        .fit()
                        .centerCrop()
                        .into(this.imgCity);
            }

            if (temperature != null)
            {
                if (weather != null)
                {
                    txtTemp.setText(String.format(Locale.getDefault(), "%.2f \u2103 (%s)", temperature.getTemp(), weather.getDescription()));
                }

                txtAvgTemp.setText(String.format(Locale.getDefault(), "%s %.2f \u2103 - %.2f \u2103", strAvgTemp,
                        temperature.getMinTemp(), temperature.getMaxTemp()));
                txtHumidity.setText(String.format(Locale.getDefault(), "%s %.2f%%", strHumidity, temperature.getHumidity()));
                txtPressure.setText(String.format(Locale.getDefault(), "%s, %.2f %s", strPressure, temperature.getPressure(), strHpa));
            }
        }
    }
}
