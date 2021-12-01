package com.example.studentcrimeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static CrimeListAdapter crimeListAdapter;
    private List<Crime> crimeList = CrimeLab.get(this).getCrimes();
    private DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
    private FloatingActionButton addCrime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCrime = findViewById(R.id.addCrime);
        addCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCrime();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        crimeListAdapter = new CrimeListAdapter(this, crimeList);
        recyclerView.setAdapter(crimeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addCrime(){
        Crime crime = new Crime();
        Date date = new Date();
        crime.setId(UUID.randomUUID());
        crime.setTitle("Crime #" + CrimeLab.get(this).getCrimes().size());
        crime.setDate(date);
        crime.setSolved(false);
        CrimeLab.get(this).addCrime(crime);
        crimeListAdapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(crimeListAdapter.getItemCount()-1);
    }
}