package com.example.physicsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private String answer;
    private TextView answerText;
    private Button showAnswerBtn;
    public static int cheatedQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        Intent intent = getIntent();
        answer = intent.getStringExtra(MainActivity.ANSWER);
        answerText = findViewById(R.id.answerText);
        showAnswerBtn = findViewById(R.id.showCheatAnswer);

        if(savedInstanceState != null) {
            answerText.setText(savedInstanceState.getString("answerText_state"));
            showAnswerBtn.setVisibility(savedInstanceState.getInt("showAnswerBtnVisibility"));
            cheatedQuestions = savedInstanceState.getInt("cheatedQuestionsCheatActivity_state");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("answerText_state", answerText.getText().toString());
        outState.putInt("showAnswerBtnVisibility", showAnswerBtn.getVisibility());
        outState.putInt("cheatedQuestionsCheatActivity_state", cheatedQuestions);
    }

    public void showAnswer(View view) {
        answerText.setText(answer);
        cheatedQuestions++;
        showAnswerBtn.setVisibility(Button.GONE);
    }

}