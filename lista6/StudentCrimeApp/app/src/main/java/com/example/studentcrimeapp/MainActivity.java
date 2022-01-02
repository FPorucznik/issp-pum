package com.example.studentcrimeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

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
    private EditText searchEditText;
    private String searchPhrase;
    public static Crime crimeToUpdate;

    public static DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCrime = findViewById(R.id.addCrime);
        searchEditText = findViewById(R.id.searchEditText);

        dbHandler = new DBHandler(this);

        getCrimes(1);

        recyclerView = findViewById(R.id.recyclerView);
        crimeListAdapter = new CrimeListAdapter(this, crimeList);
        recyclerView.setAdapter(crimeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                Date date = new Date();
                crime.setId(UUID.randomUUID());
                crime.setTitle("Crime #" + crimeList.size());
                crime.setDate(date);
                crime.setSolved(false);
                dbHandler.addCrime(crime);
                getCrimes(1);
                crimeListAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(crimeListAdapter.getItemCount()-1);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                searchPhrase = searchEditText.getText().toString();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHandler.close();
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(crimeToUpdate != null) {
            dbHandler.updateCrime(crimeToUpdate.getId(), crimeToUpdate.getTitle(), crimeToUpdate.getDate(), crimeToUpdate.getSolved());
        }
        getCrimes(1);
        crimeListAdapter.notifyDataSetChanged();
    }

    //1-all crimes, 2- search mode
    private void getCrimes(int mode){
        crimeList.clear();
        Cursor cursor;
        if(mode==1){
            cursor = dbHandler.getCrimes();
        }
        else{
            cursor = dbHandler.searchedCrimes(searchPhrase);
        }

        if(cursor.getCount() == 0){
            Toast.makeText(this,"EMPTY",Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                UUID id = UUID.fromString(cursor.getString(1));
                String title = cursor.getString(2);
                Date date = new Date(cursor.getString(3));
                Boolean solved = cursor.getInt(4) > 0;

                Crime crime = new Crime();
                crime.setId(id);
                crime.setTitle(title);
                crime.setDate(date);
                crime.setSolved(solved);

                crimeList.add(crime);
            }
        }
    }

    public void searchCrimes(View view) {
        getCrimes(2);
        crimeListAdapter.notifyDataSetChanged();
    }
}