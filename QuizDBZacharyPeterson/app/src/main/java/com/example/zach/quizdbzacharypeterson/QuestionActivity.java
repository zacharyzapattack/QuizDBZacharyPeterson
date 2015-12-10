package com.example.zach.quizdbzacharypeterson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class QuestionActivity extends AppCompatActivity {
    long position;
    QuizDB db;
    Question question;
    TextView questionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        position = getIntent().getExtras().getLong("position");
        db = ShareData.get(this).getQuizDB();
        question = db.getQuestion((int)position);
        questionText = (TextView) findViewById(R.id.textViewQuestionText);
        questionText.setText(question.getText());
    }

    public void buttonYesClick(View v){
        question.makeGuess(true);
        db.updateQuiz(question);
          
    }

    public void buttonNoClick(View v){

    }
}
