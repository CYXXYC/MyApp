package com.example.administrator.myapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button learn;
    private Button exercise;
    private Button play;
    private Button note;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        learn = (Button)findViewById(R.id.Learning);
        exercise = (Button)findViewById(R.id.Exercising);
        play = (Button)findViewById(R.id.Playing);
        note = (Button)findViewById(R.id.Noting);

        learn.setOnClickListener(this);
        exercise.setOnClickListener(this);
        play.setOnClickListener(this);
        note.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Learning:
                intent = new Intent(this,LearnActivity.class);
                break;
            case R.id.Exercising:
                intent = new Intent(this,ExerciseActivity.class);
                break;
            case R.id.Playing:
                intent = new Intent(this,PlayActivity.class);
                break;
            case R.id.Noting:
                intent = new Intent(this, AgendaActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
