package com.example.countriesmvvm.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountriesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Country> countries);

    @Query("SELECT * FROM countries ORDER BY country_name ASC")
    LiveData<List<Country>> getAll();
}
