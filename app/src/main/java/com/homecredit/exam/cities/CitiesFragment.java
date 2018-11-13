package com.homecredit.exam.cities;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.homecredit.exam.R;
import com.homecredit.exam.api.ApiCallback;
import com.homecredit.exam.api.calls.CityCall;
import com.homecredit.exam.models.City;
import com.homecredit.exam.utils.AppLogger;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesFragment extends Fragment {

    private static final String TAG = CitiesFragment.class.getSimpleName();
    private Unbinder unbinder;
    private CityCall cityCall;

    @BindView(R.id.back) ImageButton back;
    @BindView(R.id.refresh) ImageButton refresh;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.error) TextView error;
    @BindString(R.string.city_list) String cityListStr;


    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        unbinder = ButterKnife.bind(this, view);
        back.setVisibility(View.INVISIBLE);
        title.setText(cityListStr);
        retrieveCities();
        return view;
    }

    @OnClick(R.id.refresh)
    public void onRefresh() {
        retrieveCities();
    }


    /**
     * Retrieves the information of the three cities (London, Prague and San Francisco)
     */
    private void retrieveCities() {
        Context ctx = getContext();
        if (ctx != null) {

            // If an existing request is being processed, cancel it
            if (cityCall != null)
            {
                cityCall.cancelCityListCallRequest();
            }
            else
            {
                cityCall = CityCall.with(ctx);
            }

            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            error.setVisibility(View.GONE);

            cityCall.retrieveCities(new ApiCallback.GetCitiesListener() {
                @Override
                public void onSuccess(List<City> cities) {
                    progressBar.setVisibility(View.GONE);
                    if (!cities.isEmpty())
                    {
                        AppLogger.info(TAG, "retrieveCities onSuccess()");
                        Context ctx = getContext();
                        if (ctx != null)
                        {
                            recyclerView.setVisibility(View.VISIBLE);
                            LinearLayoutManager llm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(llm);
                            CitiesAdapter adapter = new CitiesAdapter(ctx, cities);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    else
                    {
                        this.onError();
                    }
                }

                @Override
                public void onError() {
                    AppLogger.error(TAG, "retrieveCities onError()");

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
