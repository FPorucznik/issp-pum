package com.example.countriesmvvm.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.countriesmvvm.database.CountryRoom;
import com.example.countriesmvvm.model.CountriesDAO;
import com.example.countriesmvvm.model.CountriesService;
import com.example.countriesmvvm.model.Country;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountryRepository {
    private CountriesDAO mCountriesDAO;

    private LiveData<List<Country>> mAllCountries;
    private final MutableLiveData<Boolean> countryError = new MutableLiveData<>();

    private CountriesService service;
    private Disposable disposable;

    public CountryRepository(Application application){
        service = new CountriesService();
        CountryRoom db = CountryRoom.getDatabase(application);
        mCountriesDAO = db.countriesDAO();
        fetchData();
        mAllCountries = mCountriesDAO.getAll();
    }

    private void fetchData() {
        disposable = service.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Country>>() {
                    @Override
                    public void onSuccess(@NonNull List<Country> values) {
                        List<Country> entities = new ArrayList<>();
                        for(Country country : values){
                            entities.add(country);
                        }

                        insertAll(entities);
                        countryError.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        countryError.setValue(true);
                    }
                });
    }

    public LiveData<List<Country>> getAllCountries(){
        return mAllCountries;
    }

    public void insertAll(List<Country> countries){
        CountryRoom.databaseWriteExecutor.execute(() -> {
            mCountriesDAO.insertAll(countries);
        });
    }

    public void onDispose(){
        disposable.dispose();
    }

    public LiveData<Boolean> getCountryError(){
        return countryError;
    }
}
