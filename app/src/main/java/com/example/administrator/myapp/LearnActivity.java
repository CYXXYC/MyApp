package com.example.administrator.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/9 0009.
 */

public class LearnActivity extends Activity {
    //四个主按钮
    private ImageView back;
    private Button studymode;
    private ImageView tasklist;
    private Button startstudy;

    //显示学习模式listview
    private List<StudyMode> modeList;
    private StudyModeAdapter studyModeAdapter;
    private StudyMode tomatostudy;
    private StudyMode sandclockstudy;
    private ListView studymodelistview;
    private View dialogView;
    private AlertDialog studymodeDialog;

    private boolean studystate = false;
    private int whichmode = 0;

    //具体学习模式的UI显示
    private TextView studyhour;
    private TextView studyminute;

    //设置学习时间
    private boolean studytime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        back = (ImageView)findViewById(R.id.back);
        studymode = (Button)findViewById(R.id.studymode);
        tasklist = (ImageView) findViewById(R.id.tasklist);
        startstudy = (Button)findViewById(R.id.studystate);

        studyhour = (TextView)findViewById(R.id.hour);
        studyhour = (TextView)findViewById(R.id.minute);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnActivity.this,MainActivity.class);
                startActivity(intent);
                onDestroy();
            }
        });

        studymode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogView();
            }
        });

        tasklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnActivity.this,TaskActivity.class);
                startActivity(intent);
                onDestroy();
            }
        });

        startstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studystate == false){
                    Toast.makeText(LearnActivity.this,"请选择学习模式",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LearnActivity.this,"Come on",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setDialogView(){
        //final Dialog studymodeDialog = new Dialog(LearnActivity.this);
        studymodeDialog = new AlertDialog.Builder(LearnActivity.this).create();
        dialogView = LayoutInflater.from(LearnActivity.this).inflate(R.layout.listview_studymode,null);
        //用ListView显示学习模式
        initStudyMode();
        studyModeAdapter = new StudyModeAdapter(LearnActivity.this,R.layout.item_studymode,modeList);
        studymodelistview = (ListView)dialogView.findViewById(R.id.studymodelist_view);
        studymodelistview.setAdapter(studyModeAdapter);
        //设置对话框显示ListView
        studymodeDialog.setView(dialogView);
        studymodeDialog.setTitle("学习模式");
        studymodeDialog.show();
        setStudyMode(studymodelistview,studymodeDialog);
    }

    //初始化学习模式list
    public void initStudyMode(){
        modeList = new ArrayList<StudyMode>();
        StudyMode tomatostudy = new StudyMode("番茄学习法",R.mipmap.tomato);
        modeList.add(tomatostudy);
        StudyMode sandlockstudy = new StudyMode("计时学习法",R.mipmap.sandclock);
        modeList.add(sandlockstudy);
    }

    //显示番茄学习模式
    public void setTomatoStudy(){
        RelativeLayout r1 = (RelativeLayout)findViewById(R.id.studylayout);
        r1.setBackgroundColor(Color.parseColor("#ff3300"));
        studymode.setText("番茄学习法");
        studymode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.tomato,0);
        //studyhour.setTextColor(Color.WHITE);
        //studyminute.setTextColor(Color.WHITE);
    }

    //显示计时学习模式
    public void setSandclockStudy(){
        RelativeLayout r1 = (RelativeLayout)findViewById(R.id.studylayout);
        r1.setBackgroundColor(Color.parseColor("#0066ff"));
        studymode.setText("计时学习法");
        studymode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.sandclock,0);
        studyhour.setTextColor(Color.parseColor("#FFFFFF"));
        studyminute.setTextColor(Color.parseColor("#FFFFFF"));
    }

    //点击选择学习模式
    public void setStudyMode(ListView listView, final AlertDialog alertDialog){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudyMode tmp = modeList.get(position);
                String name = tmp.getModeText();
                if ("番茄学习法".equals(name)){
                    //番茄学习
                    setTomatoStudy();
                    //studystate = true;
                }else if ("计时学习法".equals(name)){
                    //计时学习
                    setSandclockStudy();
                    //studystate = true;
                }
                studystate = true;
                alertDialog.dismiss();
            }
        });
    }
}
