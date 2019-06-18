package com.example.administrator.myapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private TextView titleText;

    private RadioButton learnButton;
    private RadioButton exerciseButton;
    private RadioButton noteButton;

    private Fragment learnFragment;
    private Fragment noteFragment;
    private Fragment exerciseFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initEvent();

        initFragment(0);

    }

    public void initEvent(){
        learnButton.setOnClickListener(this);
        exerciseButton.setOnClickListener(this);
        noteButton.setOnClickListener(this);
    }

    public void initView(){
        learnButton = (RadioButton)findViewById(R.id.learnradio);
        exerciseButton = (RadioButton)findViewById(R.id.exerciseradio);
        noteButton = (RadioButton)findViewById(R.id.noteradio);
        titleText = (TextView)findViewById(R.id.titletext);

        //主页面为学习fragment，设置标题栏和按钮颜色
        titleText.setLetterSpacing(1.0f);//设置标题文字间距
        titleText.setText(R.string.learnfrag);
        learnButton.setTextColor(0xff00ff00);
    }



    public void initFragment(int index){
        //Fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //初始化之前先隐藏所有fragment
        hideFragment(transaction);

        switch (index){
            case 0:
                if (learnFragment == null){
                    learnFragment = new LearnFragment();
                    transaction.add(R.id.fragment,learnFragment);
                }else{
                    transaction.show(learnFragment);
                }
                break;
            case 1:
                if (noteFragment == null){
                    noteFragment = new NoteFragment();
                    transaction.add(R.id.fragment,noteFragment);
                }else{
                    transaction.show(noteFragment);
                }
                break;
            case 2:
                if (exerciseFragment == null){
                    exerciseFragment = new ExerciseFragment();
                    transaction.add(R.id.fragment,exerciseFragment);
                }else{
                    transaction.show(exerciseFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (learnFragment != null){
            transaction.hide(learnFragment);
        }
        if (noteFragment != null){
            transaction.hide(noteFragment);
        }
        if (exerciseFragment != null){
            transaction.hide(exerciseFragment);
        }
    }

    @Override
    public void onClick(View v) {
        restartButton();//每次点击前将所有文字按钮设为黑色
        switch (v.getId()){
            case R.id.learnradio:
                initFragment(0);
                learnButton.setTextColor(0xff00ff00);
                titleText.setText(getString(R.string.learnfrag));
                break;
            case R.id.noteradio:
                initFragment(1);
                noteButton.setTextColor(0xff00ff00);
                titleText.setText(getString(R.string.notefrag));
                break;
            case R.id.exerciseradio:
                initFragment(2);
                titleText.setText(getString(R.string.dongfrag));
                exerciseButton.setTextColor(0xff00ff00);
                break;
        }
    }

    private void restartButton(){
        learnButton.setTextColor(0xff000000);
        exerciseButton.setTextColor(0xff000000);
        noteButton.setTextColor(0xff000000);
    }
}
