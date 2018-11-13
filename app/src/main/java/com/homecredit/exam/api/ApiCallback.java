package com.homecredit.exam.api;

import com.homecredit.exam.models.City;

import java.util.List;

public class ApiCallback {

    public interface GetCitiesListener
    {
        void onSuccess(List<City> cities);
        void onError();
    }

    public interface GetCityListener
    {
        void onSuccess(City city);
        void onError();
    }
}
