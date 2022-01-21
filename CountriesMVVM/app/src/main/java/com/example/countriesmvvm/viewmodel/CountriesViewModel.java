package com.example.countriesmvvm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.countriesmvvm.model.CountriesService;
import com.example.countriesmvvm.model.Country;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountriesViewModel extends ViewModel {

    private final MutableLiveData<List<String>> countries = new MutableLiveData<>();
    private final MutableLiveData<Boolean> countryError = new MutableLiveData<>();

    private CountriesService service;
    private Disposable disposable;

    public CountriesViewModel(){
        service = new CountriesService();
        fetchData();
    }

    private void fetchData() {
        disposable = service.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Country>>() {
                    @Override
                    public void onSuccess(@NonNull List<Country> values) {
                        //add country capital next update
                        List<String> countryNames = new ArrayList<>();
                        for(Country country : values){
                            countryNames.add(country.countryName);
                        }

                        countries.setValue(countryNames);
                        countryError.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        countryError.setValue(true);
                    }
                });
    }

    public LiveData<List<String>> getCountries(){
        return countries;
    }

    public LiveData<Boolean> getCountryError(){
        return countryError;
    }

    public void onDispose(){
        disposable.dispose();
    }
}
