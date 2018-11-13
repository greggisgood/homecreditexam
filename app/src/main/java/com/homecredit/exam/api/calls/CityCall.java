package com.homecredit.exam.api.calls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.homecredit.exam.api.ApiCallback;
import com.homecredit.exam.api.ApiConnector;
import com.homecredit.exam.api.ApiHandler;
import com.homecredit.exam.cache.CacheManager;
import com.homecredit.exam.constants.Values;
import com.homecredit.exam.models.City;
import com.homecredit.exam.models.api.CityList;
import com.homecredit.exam.utils.AppLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityCall {
    private static final String TAG = CityCall.class.getSimpleName();
    private Context context;
    private CacheManager manager;

    private Call<CityList> cityListCall;
    private Call<City> cityCall;

    public static CityCall with(Context context) {
        return new CityCall(context);
    }

    private CityCall(Context context) {
        this.context = context;
        manager = CacheManager.with(context);
    }

    /**
     * Retrieves the details of the cities Prague, San Francisco and London
     * @param listener - Listener for callback events
     */
    public void retrieveCities(@NonNull ApiCallback.GetCitiesListener listener) {
        ApiHandler api = ApiConnector.createService(ApiHandler.class);

        Map<String, String> params = new HashMap<>();
        params.put("units", "metric"); // Set to Metric to change the temperature values to Celsius
        params.put("id", TextUtils.join(",", Arrays.asList("3067696", "5391959", "2643743"))); // These IDs are from Prague, San Francisco and London, respectively
        params.put("appid", Values.API_KEY); // App ID is set to query the necessary data

        cityListCall = api.getCities(params);
        cityListCall.enqueue(new Callback<CityList>() {
            @Override
            public void onResponse(@NonNull Call<CityList> call, @NonNull Response<CityList> response) {
                AppLogger.info(TAG, "retrieveCities onResponse()");
                if (response.isSuccessful())
                {
                    CityList cityList = response.body();
                    if (cityList != null && !cityList.getCityList().isEmpty())
                    {
                        List<City> cities = cityList.getCityList();
                        Gson gson = new Gson();
                        String listStr = gson.toJson(cities);
                        manager.writeToFile(listStr, Values.CITIES_LIST);

                        listener.onSuccess(cities);
                    }
                    else
                    {
                        listener.onError();
                    }
                }
                else
                {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityList> call, @NonNull Throwable t) {
                if (call.isCanceled())
                {
                    AppLogger.error(TAG, "retrieveCities canceled");
                }
                else
                {
                    t.printStackTrace();
                    AppLogger.error(TAG, "retrieveCities onFailure()");
                    listener.onError();
                }
            }
        });
    }

    /**
     * Retrieves the details of one city, based on the City ID
     * @param id - The City ID
     * @param listener - The Listener for callback events
     */
    public void getCity(String id, @NonNull ApiCallback.GetCityListener listener)
    {
        ApiHandler api = ApiConnector.createService(ApiHandler.class);

        Map<String, String> params = new HashMap<>();
        params.put("units", "metric"); // Set to Metric to change the temperature values to Celsius
        params.put("id", id); // The ID of the city in question
        params.put("appid", Values.API_KEY); // App ID is set to query the necessary data

        cityCall = api.getCity(params);
        cityCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(@NonNull Call<City> call, @NonNull Response<City> response) {
                AppLogger.info(TAG, "getCity onResponse()");
                if (response.isSuccessful())
                {
                    City city = response.body();
                    if (city != null)
                    {
                        listener.onSuccess(city);
                    }
                    else
                    {
                        listener.onError();
                    }
                }
                else
                {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<City> call, @NonNull Throwable t) {
                if (cityCall.isCanceled())
                {
                    AppLogger.error(TAG, "retrieveCity canceled");
                }
                else
                {
                    t.printStackTrace();
                    AppLogger.error(TAG, "getCity onFailure()");
                    listener.onError();
                }
            }
        });
    }

    /**
     * Cancels the call to retrieve a city's information
     */
    public void cancelCityCallRequest()
    {
        if (cityCall != null)
        {
            cityCall.cancel();
        }
    }

    /**
     * Cancels the call to retrieve information of multiple cities
     */
    public void cancelCityListCallRequest()
    {
        if (cityListCall != null)
        {
            cityListCall.cancel();
        }
    }
}
