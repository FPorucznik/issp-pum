package com.example.physicsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static final String ANSWER = "com.example.physicsquiz.ANSWER";

    private TextView questionText;
    private TextView correctText;
    private TextView incorrectText;
    private TextView cheatedText;
    private TextView scoreText;

    private Button btnTrue;
    private Button btnFalse;
    private Button btnPrev;
    private Button btnNext;
    private Button btnCheat;
    private Button btnReset;
    private Button btnShow;

    private int currentQuestion;
    private int answeredQuestions;
    private int score;

    private final Question[] questions = new Question[]{
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, false),
            new Question(R.string.question4, true),
            new Question(R.string.question5, true),
            new Question(R.string.question6, false)
    };

    private Boolean[] answers = new Boolean[questions.length];
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.questionText);
        questionText.setText(questions[0].getTextId());

        correctText = findViewById(R.id.correctText);
        incorrectText = findViewById(R.id.incorrectText);
        cheatedText = findViewById(R.id.cheatedText);
        scoreText = findViewById(R.id.scoreText);

        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnCheat = findViewById(R.id.btnCheat);
        btnReset = findViewById(R.id.btnReset);
        btnShow = findViewById(R.id.btnShow);

        currentQuestion = 0;
        answeredQuestions = 0;
        score = 0;

        if(savedInstanceState != null) {
            questionText.setText(savedInstanceState.getString("questionText_state"));
            currentQuestion = savedInstanceState.getInt("currentQuestion_state");
            answeredQuestions = savedInstanceState.getInt("answeredQuestions_state");
            score = savedInstanceState.getInt("score_state");

            answers = (Boolean[]) savedInstanceState.getSerializable("answers_state");

            btnTrue.setVisibility(savedInstanceState.getInt("btnTrue_state"));
            btnFalse.setVisibility(savedInstanceState.getInt("btnFalse_state"));
            btnPrev.setVisibility(savedInstanceState.getInt("btnPrev_state"));
            btnNext.setVisibility(savedInstanceState.getInt("btnNext_state"));
            btnCheat.setVisibility(savedInstanceState.getInt("btnCheat_state"));
            btnReset.setVisibility(savedInstanceState.getInt("btnReset_state"));
            btnShow.setVisibility(savedInstanceState.getInt("btnShow_state"));

            correctText.setText(savedInstanceState.getString("correctText_state"));
            incorrectText.setText(savedInstanceState.getString("incorrectText_state"));
            cheatedText.setText(savedInstanceState.getString("cheatedText_state"));
            scoreText.setText(savedInstanceState.getString("scoreText_state"));

            correctText.setVisibility(savedInstanceState.getInt("correctTextVisibility_state"));
            incorrectText.setVisibility(savedInstanceState.getInt("incorrectTextVisibility_state"));
            cheatedText.setVisibility(savedInstanceState.getInt("cheatedTextVisibility_state"));
            scoreText.setVisibility(savedInstanceState.getInt("scoreTextVisibility_state"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("currentQuestion_state", currentQuestion);
        outState.putInt("answeredQuestions_state", answeredQuestions);
        outState.putInt("score_state", score);
        outState.putString("questionText_state", questionText.getText().toString());
        outState.putSerializable("answers_state", answers);

        outState.putInt("btnTrue_state", btnTrue.getVisibility());
        outState.putInt("btnFalse_state", btnFalse.getVisibility());
        outState.putInt("btnPrev_state", btnPrev.getVisibility());
        outState.putInt("btnNext_state", btnNext.getVisibility());
        outState.putInt("btnCheat_state", btnCheat.getVisibility());
        outState.putInt("btnReset_state", btnReset.getVisibility());
        outState.putInt("btnShow_state", btnShow.getVisibility());

        outState.putString("correctText_state", correctText.getText().toString());
        outState.putString("incorrectText_state", incorrectText.getText().toString());
        outState.putString("cheatedText_state", cheatedText.getText().toString());
        outState.putString("scoreText_state", scoreText.getText().toString());

        outState.putInt("correctTextVisibility_state", correctText.getVisibility());
        outState.putInt("incorrectTextVisibility_state", incorrectText.getVisibility());
        outState.putInt("cheatedTextVisibility_state", cheatedText.getVisibility());
        outState.putInt("scoreTextVisibility_state", scoreText.getVisibility());
    }

    public void nextQuestion(View view) {
        currentQuestion++;
        if(currentQuestion == questions.length){
            currentQuestion = 0;
        }
        buttonToggle();
        questionText.setText(questions[currentQuestion].getTextId());
    }

    public void prevQuestion(View view) {
        currentQuestion--;
        if(currentQuestion < 0){
            currentQuestion = questions.length-1;
        }
        buttonToggle();
        questionText.setText(questions[currentQuestion].getTextId());
    }

    public void questionAnswer(View view) {
        answeredQuestions++;
        boolean ans = Boolean.parseBoolean(((Button) view).getText().toString().toLowerCase());
        answers[currentQuestion] = ans;
        buttonToggle();
        btnShow.setVisibility(Button.VISIBLE);
        btnCheat.setVisibility(Button.GONE);

        if(ans == questions[currentQuestion].isAnswer()){
            score++;
            Snackbar info = Snackbar.make(findViewById(R.id.questionText), "Correct", Snackbar.LENGTH_SHORT);
            info.show();
        }
        else{
            Snackbar info = Snackbar.make(findViewById(R.id.questionText), "Incorrect", Snackbar.LENGTH_SHORT);
            info.show();
        }
        summary();
    }

    public void buttonToggle(){
        if(answers[currentQuestion] != null){
            btnTrue.setVisibility(Button.GONE);
            btnFalse.setVisibility(Button.GONE);
            btnCheat.setVisibility(Button.GONE);
            btnShow.setVisibility(Button.VISIBLE);
        }
        else{
            btnTrue.setVisibility(Button.VISIBLE);
            btnFalse.setVisibility(Button.VISIBLE);
            btnCheat.setVisibility(Button.VISIBLE);
            btnShow.setVisibility(Button.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void summary(){
        if(answeredQuestions == 6){
            double result;
            double scoreDouble = score;
            double minusPercent = CheatActivity.cheatedQuestions;
            minusPercent *= 15;

            if(minusPercent > 100 || (((scoreDouble/6)*100) - minusPercent) < 0){
                result = 0;
            }
            else{
                result = ((scoreDouble/6)*100) - minusPercent;
            }

            toggleView(false);

            questionText.setText("Quiz Summary");
            correctText.setText("Correct answers: " + score);
            incorrectText.setText("Incorrect answers: " + (6-score));
            cheatedText.setText("Answers cheated: " + CheatActivity.cheatedQuestions);
            scoreText.setText("Score: " + df.format(result) + "%");
        }
    }

    public void toggleView(boolean quiz){
        if(quiz){
            btnTrue.setVisibility(Button.VISIBLE);
            btnFalse.setVisibility(Button.VISIBLE);
            btnPrev.setVisibility(Button.VISIBLE);
            btnNext.setVisibility(Button.VISIBLE);
            btnCheat.setVisibility(Button.VISIBLE);
            correctText.setVisibility(TextView.GONE);
            incorrectText.setVisibility(TextView.GONE);
            cheatedText.setVisibility(TextView.GONE);
            scoreText.setVisibility(TextView.GONE);
            btnReset.setVisibility(Button.GONE);
        }
        else{
            btnTrue.setVisibility(Button.GONE);
            btnFalse.setVisibility(Button.GONE);
            btnPrev.setVisibility(Button.GONE);
            btnNext.setVisibility(Button.GONE);
            btnCheat.setVisibility(Button.GONE);
            btnShow.setVisibility(Button.GONE);
            correctText.setVisibility(TextView.VISIBLE);
            incorrectText.setVisibility(TextView.VISIBLE);
            cheatedText.setVisibility(TextView.VISIBLE);
            scoreText.setVisibility(TextView.VISIBLE);
            btnReset.setVisibility(Button.VISIBLE);
        }
    }

    public void resetQuiz(View view) {
        toggleView(true);
        score = 0;
        answeredQuestions = 0;
        currentQuestion = 0;
        CheatActivity.cheatedQuestions = 0;
        questionText.setText(questions[0].getTextId());
        Arrays.fill(answers, null);
    }

    public void showCheatActivity(View view) {
        Intent intent = new Intent(this, CheatActivity.class);
        intent.putExtra(ANSWER, Boolean.toString(questions[currentQuestion].isAnswer()));
        startActivity(intent);
    }

    public void showCorrectAnswer(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Correct Answer");
        alertDialog.setMessage(Boolean.toString(questions[currentQuestion].isAnswer()));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}