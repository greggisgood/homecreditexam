package com.homecredit.exam.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.homecredit.exam.models.City;

import java.util.List;

public class CityList {
    @SerializedName("list") @Expose
    private List<City> cityList;

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
