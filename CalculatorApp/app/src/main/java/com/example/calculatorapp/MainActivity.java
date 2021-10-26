package com.example.calculatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private boolean activeOperator;
    private String operator;
    private String numOne;
    private String numTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        activeOperator = false;
        operator = "";
        numOne = "";
        numTwo = "";

        if(savedInstanceState != null) {
            textView.setText(savedInstanceState.getString("calcView_state"));
            activeOperator = savedInstanceState.getBoolean("calcActiveOperator_state");
            operator = savedInstanceState.getString("calcOperator_state");
            numOne = savedInstanceState.getString("calcNumOne_state");
            numTwo = savedInstanceState.getString("calcNumTwo_state");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("calcView_state", textView.getText().toString());
        outState.putString("calcNumOne_state", numOne);
        outState.putString("calcNumTwo_state", numTwo);
        outState.putString("calcOperator_state", operator);
        outState.putBoolean("calcActiveOperator_state", activeOperator);
    }

    public void onClickNumber(View view) {
        if(activeOperator){
            numTwo += ((Button) view).getText().toString();
        }
        else{
            numOne += ((Button) view).getText().toString();
        }
        textView.append(((Button) view).getText());
    }

    @SuppressLint("SetTextI18n")
    public void onClickOperator(View view) {
        if(!textView.getText().toString().isEmpty() && !((Button) view).getText().toString().equals("=") && !activeOperator ){
            numOne = textView.getText().toString();
            textView.append(((Button) view).getText());
            operator = ((Button) view).getText().toString();
            activeOperator = true;
        }
        if(((Button) view).getText().toString().equals("=") && !numOne.isEmpty() && !numTwo.isEmpty()){
            double result = 0.0;
            switch (operator){
                case "+":
                    result = Double.parseDouble(numOne) + Double.parseDouble(numTwo);
                    textView.setText(Double.toString(result));
                    break;
                case "-":
                    result = Double.parseDouble(numOne) - Double.parseDouble(numTwo);
                    textView.setText(Double.toString(result));
                    break;
                case "*":
                    result = Double.parseDouble(numOne) * Double.parseDouble(numTwo);
                    textView.setText(Double.toString(result));
                    break;
                case "/":
                    if(Double.parseDouble(numTwo) == 0){
                        Snackbar info = Snackbar.make(findViewById(R.id.textView), "Cannot divide by 0", Snackbar.LENGTH_SHORT);
                        info.show();
                        result = Double.parseDouble(numOne);
                        textView.setText(numOne);
                    }
                    else{
                        result = Double.parseDouble(numOne) / Double.parseDouble(numTwo);
                        textView.setText(Double.toString(result));
                    }
                    break;
            }
            numOne = Double.toString(result);
            numTwo = "";
            activeOperator = false;
        }
    }

    public void onClickClear(View view) {
        textView.setText("");
        activeOperator = false;
        numOne = "";
        numTwo = "";
    }
}