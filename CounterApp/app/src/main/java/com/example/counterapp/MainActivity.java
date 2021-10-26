package com.example.counterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int count;
    private TextView showCount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCount = findViewById(R.id.show_count);

        if(savedInstanceState != null){
            count = savedInstanceState.getInt("counter_state");

            if(showCount != null){
                showCount.setText(Integer.toString(count));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void countUpButton(View view) {
        count++;
        if(showCount != null){
            showCount.setText(Integer.toString(count));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("counter_state", count);
    }

    @SuppressLint("SetTextI18n")
    public void countDownButton(View view) {
        count--;
        if(showCount != null){
            showCount.setText(Integer.toString(count));
        }
    }

    @SuppressLint("SetTextI18n")
    public void resetButton(View view) {
        count = 0;
        if(showCount != null){
            showCount.setText(Integer.toString(count));
        }
    }
}