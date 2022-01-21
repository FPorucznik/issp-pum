package com.example.countriesmvvm.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.countriesmvvm.model.CountriesService;
import com.example.countriesmvvm.model.Country;
import com.example.countriesmvvm.repository.CountryRepository;
import com.example.countriesmvvm.view.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountriesViewModel extends ViewModel {

    private CountryRepository repository;
    private LiveData<List<Country>> countries;

    public CountriesViewModel(){
        countries = null;
    }

    public void initRepo(Application application){
        repository = new CountryRepository(application);
        countries = repository.getAllCountries();
    }

    public LiveData<List<Country>> getCountries(){
        return countries;
    }

    public LiveData<Boolean> getCountryError(){
        return repository.getCountryError();
    }

    public void onDispose(){
        repository.onDispose();
    }
}
