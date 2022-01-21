package com.example.countriesmvvm.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "countries")
public class Country {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "country_name")
    @SerializedName("name")
    public String countryName;

    @ColumnInfo(name = "country_capital")
    @SerializedName("capital")
    public String countryCapital;

    public Country(){
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getCountryCapital(){
        return this.countryCapital;
    }
}
