package com.example.studentcrimeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import java.util.UUID;

public class CrimeActivity extends AppCompatActivity {

    private UUID crimeId;
    public CrimeViewPagerAdapter adapter;
    public ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_view_pager2);

        crimeId = UUID.fromString(getIntent().getStringExtra("id"));
        Crime activeCrime = CrimeLab.get(this).getCrime(crimeId);

        viewPager2 = findViewById(R.id.detail_view_pager);
        adapter = new CrimeViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        if(activeCrime != null){
            viewPager2.setCurrentItem(CrimeLab.get(this).getCrimes().indexOf(activeCrime), false);
        }
    }

    public void showDateDialog(View view){
        adapter.showDateDialog(view);
    }

    public void removeCrime(View view) {
        adapter.removeCrime(view);
        finish();
    }

    public void firstCrime(View view) {
        viewPager2.setCurrentItem(0);
    }

    public void lastCrime(View view) {
        viewPager2.setCurrentItem(CrimeLab.get(CrimeActivity.this).getCrimes().size());
    }
}
