package com.example.studentcrimeapp;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        Date date = new Date();

        for(int i = 0; i < 50; i++){
            Crime crime = new Crime();
            crime.setId(UUID.randomUUID());
            crime.setTitle("Crime #" + i);
            crime.setDate(date);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime : mCrimes){
            if(crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void removeCrime(Crime crime){
        mCrimes.remove(crime);
    }

    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }
}
