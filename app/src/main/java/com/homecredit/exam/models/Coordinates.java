package com.homecredit.exam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lat") @Expose
    private Double lat;
    @SerializedName("lon") @Expose
    private Double longitude;
}
