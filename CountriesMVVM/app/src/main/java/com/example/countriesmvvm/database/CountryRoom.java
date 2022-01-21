package com.example.countriesmvvm.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.countriesmvvm.model.CountriesDAO;
import com.example.countriesmvvm.model.Country;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Country.class}, version = 2, exportSchema = false)
public abstract class CountryRoom  extends RoomDatabase {

    public abstract CountriesDAO countriesDAO();

    private static volatile CountryRoom INSTANCE;
    private static final int NUM_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static CountryRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), CountryRoom.class, "countries_database")
                    .build();
        }
        return INSTANCE;
    }
}