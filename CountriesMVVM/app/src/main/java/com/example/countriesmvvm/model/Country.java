package com.example.countriesmvvm.model;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("name")
    public String countryName;

    @SerializedName("capital")
    public String countryCapital;
}
