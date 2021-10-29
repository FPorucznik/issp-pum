package com.example.physicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private int currentQuestion;

    private final Question[] questions = new Question[]{
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, false),
            new Question(R.string.question4, true),
            new Question(R.string.question5, true),
            new Question(R.string.question6, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.questionText);
        textView.setText(questions[0].getTextId());

        currentQuestion = 0;
    }

    public void nextQuestion(View view) {
        currentQuestion++;
        if(currentQuestion == questions.length){
            currentQuestion = 0;
        }
        textView.setText(questions[currentQuestion].getTextId());
    }

    public void prevQuestion(View view) {
        currentQuestion--;
        if(currentQuestion < 0){
            currentQuestion = questions.length-1;
        }
        textView.setText(questions[currentQuestion].getTextId());
    }
}