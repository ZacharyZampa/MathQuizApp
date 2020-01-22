package com.zampaze.mathquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    Button startButton;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button restartButton;
    TextView timeView;
    TextView problemView;
    TextView pNumberView;
    TextView outcomeView;
    TextView scoreView;

    ArrayList<Integer> answers = new ArrayList<>(4);
    int buttonWithCorrectAnswer;
    int score;
    int numberOfQuestions;
    int roundNumber;
    Random rand;
    CountDownTimer timer;

    public void timerSetup() {
        timer = new CountDownTimer(20100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                finished();
            }
        }.start();
    }

    public void start(View view) {
        // Change visibilities of buttons to game mode
        startButton.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);

        // Show text view stats
        timeView.setVisibility(View.VISIBLE);
        problemView.setVisibility(View.VISIBLE);
        pNumberView.setVisibility(View.VISIBLE);

        // start timer
        timerSetup();
    }

    public void restart(View view) {
        score = 0;
        timeView.setText("");
        numberOfQuestions = rand.nextInt(15) + 3;
        roundNumber = 1;
        enableDisablePlayButtons(true);
        timerSetup();
        newQuestion();
    }

    public void chooseAnswer(View view) {
        if (view.getTag().toString().equals(Integer.toString(buttonWithCorrectAnswer))) {
            // correct answer
            outcomeView.setText("Correct!");
            score++;
        } else {
            // incorrect answer
            outcomeView.setText("Incorrect");
        }
        roundNumber++;
        pNumberView.setText(roundNumber + "/" + numberOfQuestions);
        scoreView.setText("SCORE: " + score + " points!");

        newQuestion();
    }

    public void finished() {
        restartButton.setVisibility(View.VISIBLE);
        timer.cancel();
        outcomeView.setText("Finished!");
        enableDisablePlayButtons(false);
    }

    public void enableDisablePlayButtons(boolean flag) {
        button1.setEnabled(flag);
        button2.setEnabled(flag);
        button3.setEnabled(flag);
        button4.setEnabled(flag);
    }

    public void newQuestion() {
        // base case - is it done?
        if (roundNumber >= numberOfQuestions) {
            // finished
            finished();
        }

        // clear array of answers
        answers.clear();

        // generate next question
        int a = rand.nextInt(21);
        int b = rand.nextInt(21);

        int answer = a + b;

        // what button the correct answer will be
        buttonWithCorrectAnswer = rand.nextInt(4);

        // populate butttons
        for (int i = 0; i < 4; i++) {
            if (i == buttonWithCorrectAnswer) {
                answers.add(answer);
            } else {
                int number = rand.nextInt(41);
                // ensure it is an acceptable answer (no duplicates)
                while (number == answer) {
                    number = rand.nextInt(41);
                }
                answers.add(number);
            }

        }

        button1.setText(Integer.toString(answers.get(0)));
        button2.setText(Integer.toString(answers.get(1)));
        button3.setText(Integer.toString(answers.get(2)));
        button4.setText(Integer.toString(answers.get(3)));
        problemView.setText(a + " + " + b);
        pNumberView.setText(roundNumber + "/" + numberOfQuestions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab UI Elements
        startButton = findViewById(R.id.startButton);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        restartButton = findViewById(R.id.restartButton);
        timeView = findViewById(R.id.timeView);
        problemView = findViewById(R.id.problemView);
        pNumberView = findViewById(R.id.pNumberView);
        outcomeView = findViewById(R.id.outcomeView);
        scoreView = findViewById(R.id.scoreView);


        // generate numbers
        rand = new Random();

        // set number of questions
        numberOfQuestions = rand.nextInt(15) + 3;
        roundNumber = 1;
        newQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                Toast.makeText(this,
                        "Try to answer all of the questions before the time runs out",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}
