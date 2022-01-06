package com.example.factorialmultithreadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private static int MAX_TIMEOUT = 300;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    private EditText inputNumberEditText;

    private Button calculateBtn;
    private TextView resultTextView;

    private int numberOfThreads;
    private volatile BigInteger[] threadsResults;
    private final AtomicInteger numberOfFinishedThreads = new AtomicInteger(0);

    private volatile boolean abortComputation;
    private long computationTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNumberEditText = findViewById(R.id.inputNumber);
        calculateBtn = findViewById(R.id.computeBtn);
        resultTextView = findViewById(R.id.resultTextView);

        calculateBtn.setOnClickListener(v -> {
            if(inputNumberEditText.getText().toString().isEmpty() || inputNumberEditText.getText().toString().contains(",") || inputNumberEditText.getText().toString().contains(".") || inputNumberEditText.getText().toString().contains(" ")){
                Toast toast = new Toast(this);
                Toast.makeText(this, "Enter a positive integer", Toast.LENGTH_SHORT).show();
            }
            else {
                if(Integer.parseInt(inputNumberEditText.getText().toString()) == 0){
                    resultTextView.setText("0");
                }
                else{
                    resultTextView.setText("");
                    calculateBtn.setEnabled(false);

                    if(Integer.parseInt(inputNumberEditText.getText().toString()) < 20){
                        List<Integer> nums = IntStream.rangeClosed(1, Integer.parseInt(inputNumberEditText.getText().toString()))
                                .boxed().collect(Collectors.toList());
                        List<List<Integer>> factorial = new ArrayList<List<Integer>>();

                        factorial.add(nums);
                        computeFactorial(factorial, getTimeout(), Integer.parseInt(inputNumberEditText.getText().toString()));
                    }
                    else{
                        List<List<Integer>> factorial = splitFactorial(Integer.parseInt(inputNumberEditText.getText().toString()));
                        computeFactorial(factorial, getTimeout(), Integer.parseInt(inputNumberEditText.getText().toString()));
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        abortComputation = true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abortComputation = true;
    }

    private int getTimeout() {
        return MAX_TIMEOUT;
    }

    private void computeFactorial(final List<List<Integer>> factorial, final int timeout, final int inputNumber) {
        new Thread(() -> {
            initParams(timeout, inputNumber);
            startComputation(factorial);
            waitForCompletionOrAbort();
            processResults();
        }).start();
    }

    private void processResults() {
        String result;

        if(abortComputation){
            result = "Aborted computation";
        }
        else{
            result = String.valueOf(computeFinalResult());
        }

        if(isTimeout()){
            result = "Computation timeout";
        }

        final String finalResult = result;
        mUiHandler.post(() -> {
            resultTextView.setText(finalResult);
            calculateBtn.setEnabled(true);
        });
    }

    private BigInteger computeFinalResult() {
        BigInteger result;

        if(numberOfThreads == 1){
            result = threadsResults[0];
        }
        else{
            result = threadsResults[0].multiply(threadsResults[1]);
        }
        return result;
    }

    private void waitForCompletionOrAbort() {
        while(true){
            if(numberOfFinishedThreads.get() == numberOfThreads){
                break;
            }
            else if(abortComputation){
                break;
            }
            else if(isTimeout()){
                break;
            }
            else{
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= computationTimeout;
    }

    private void startComputation(List<List<Integer>> factorial) {

        for(int i = 0; i < numberOfThreads; i++){
            final int threadIndex = i;

            new Thread(() -> {
                BigInteger result = BigInteger.ONE;

                for(int j = 0; j < factorial.get(threadIndex).size(); j++){
                    result = result.multiply(BigInteger.valueOf(factorial.get(threadIndex).get(j)));
                }

                threadsResults[threadIndex] = result;
                numberOfFinishedThreads.incrementAndGet();

            }).start();
        }
    }

    private void initParams(int timeout, int inputNumber) {
        numberOfThreads = inputNumber < 20 ? 1 : 2;
        numberOfFinishedThreads.set(0);
        abortComputation = false;
        threadsResults = new BigInteger[numberOfThreads];
        computationTimeout = System.currentTimeMillis() + timeout;
    }

    private List<List<Integer>> splitFactorial(int number) {
        List<List<Integer>> result = new ArrayList<>();

        List<Integer> firstHalf = IntStream.rangeClosed(1, number/2)
                .boxed().collect(Collectors.toList());
        List<Integer> secondHalf = IntStream.rangeClosed((number/2)+1, number)
                .boxed().collect(Collectors.toList());

        result.add(firstHalf);
        result.add(secondHalf);

        return result;
    }

}