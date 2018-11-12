package com.homecredit.exam.api.calls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.homecredit.exam.api.ApiCallback;
import com.homecredit.exam.api.ApiConnector;
import com.homecredit.exam.api.ApiHandler;
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
    private Call<CityList> call;

    public static CityCall with(Context context) {
        return new CityCall(context);
    }

    private CityCall(Context context) {
        this.context = context;
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

        call = api.getCities(params);
        call.enqueue(new Callback<CityList>() {
            @Override
            public void onResponse(@NonNull Call<CityList> call, @NonNull Response<CityList> response) {
                AppLogger.info(TAG, "retrieveCities onResponse()");
                if (response.isSuccessful())
                {
                    CityList cityList = response.body();
                    if (cityList != null)
                    {
                        listener.onSuccess(cityList.getCityList());
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
                t.printStackTrace();
                if (call.isCanceled())
                {
                    AppLogger.error(TAG, "retrieveCities canceled");
                }
                else
                {
                    AppLogger.error(TAG, "retrieveCities onFailure()");
                    listener.onError();
                }
            }
        });
    }

    public void cancelRequest()
    {
        if (call != null)
        {
            call.cancel();
        }
    }
}
