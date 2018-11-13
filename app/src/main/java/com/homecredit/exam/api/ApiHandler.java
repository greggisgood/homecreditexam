package com.homecredit.exam.api;

import com.homecredit.exam.constants.Values;
import com.homecredit.exam.models.City;
import com.homecredit.exam.models.api.CityList;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiHandler {

    // Retrieves the details of multiple cities
    @GET(Values.GROUP_URL)
    Call<CityList> getCities(@QueryMap Map<String, String> params);

    // Retrieves the details of one city
    @GET(Values.WEATHER_URL)
    Call<City> getCity(@QueryMap Map<String, String> params);

}
