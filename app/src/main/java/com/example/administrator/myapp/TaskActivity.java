package com.example.administrator.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2019/5/16 0016.
 */

public class TaskActivity extends AppCompatActivity{

    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        back = (ImageView)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(TaskActivity.this, StudyActivity.class);
                //startActivity(intent);
                finish();//返回之后还能维持刚刚的学习设置状态，因此不用intent
            }
        });

    }
}
