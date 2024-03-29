package com.example.administrator.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.math.RoundingMode.UP;

/**
 * Created by Administrator on 2019/5/9 0009.
 */

public class LearnActivity extends AppCompatActivity {
    //四个主按钮和界面
    private ImageView back;
    private Button studymode;
    private ImageView tasklist;
    private Button startstudy;
    private RelativeLayout r1;
    private RelativeLayout rstudytime;

    //显示学习模式listview
    private List<StudyMode> modeList;
    private StudyModeAdapter studyModeAdapter;
    private StudyMode tomatostudy;
    private StudyMode sandclockstudy;
    private ListView studymodelistview;
    private View dialogView;
    private Dialog studymodeDialog;

    //显示时间设置对话框
    private View settimeView;
    private TimePicker timePicker;


    //具体学习模式的UI显示
    private TextView studyhour;
    private TextView studyminute;

    //判断是否设置学习状态和学习时间，是否进入学习
    private boolean studymodestate = false;
    private int whichmode = -1;
    private boolean studytimestate = false;
    private boolean isstudystate = false;

    //handler设置学习模式和时间
    private final int MODE_UI = 0;
    private final int MODE_SETTIME = 1;

    //惩罚音乐
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        back = (ImageView)findViewById(R.id.back);
        studymode = (Button)findViewById(R.id.studymode);
        tasklist = (ImageView) findViewById(R.id.tasklist);
        startstudy = (Button)findViewById(R.id.studystate);
        r1 = (RelativeLayout)findViewById(R.id.studylayout);
        rstudytime = (RelativeLayout)findViewById(R.id.studytime);

        studyhour = (TextView)findViewById(R.id.hour);
        studyminute = (TextView)findViewById(R.id.minute);

        mediaPlayer = MediaPlayer.create(LearnActivity.this,R.raw.punish_music);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isstudystate == false) {
                    Intent intent = new Intent(LearnActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    //学习没结束跳出的惩罚
                    quitStudyView();
                }
            }
        });

        studymode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isstudystate == true){
                    Toast.makeText(LearnActivity.this,"本轮学习还没结束",Toast.LENGTH_LONG).show();
                }else {
                    setDialogView();
                }
            }
        });

        rstudytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studymodestate == false){
                    Toast.makeText(LearnActivity.this,"请选择学习模式",Toast.LENGTH_LONG).show();
                }else if (isstudystate == true){
                    Toast.makeText(LearnActivity.this,"本轮学习还没结束",Toast.LENGTH_LONG).show();
                }else {
                    setStudyTime();
                }
            }
        });

        tasklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnActivity.this,TaskActivity.class);
                startActivity(intent);

            }
        });

        startstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studymodestate == false){
                    Toast.makeText(LearnActivity.this,"请选择学习模式",Toast.LENGTH_LONG).show();
                }else if (studytimestate == false) {
                    Toast.makeText(LearnActivity.this,"请设置学习时间",Toast.LENGTH_LONG).show();
                }else{
                    //进入学习状态
                    isstudystate = true;
                    Toast.makeText(LearnActivity.this,"Come on",Toast.LENGTH_LONG).show();
                    startstudy.setText("正在学习");
                }
            }
        });
    }

    public void setDialogView(){
        //final Dialog studymodeDialog = new Dialog(LearnActivity.this);
        //studymodeDialog = new AlertDialog.Builder(LearnActivity.this).create();
        studymodeDialog = new Dialog(LearnActivity.this);
        dialogView = LayoutInflater.from(LearnActivity.this).inflate(R.layout.listview_studymode,null);
        //用ListView显示学习模式
        initStudyMode();
        studyModeAdapter = new StudyModeAdapter(LearnActivity.this,R.layout.item_studymode,modeList);
        studymodelistview = dialogView.findViewById(R.id.studymodelist_view);
        studymodelistview.setAdapter(studyModeAdapter);
        //设置对话框显示ListView
        //studymodeDialog.setView(dialogView);
        studymodeDialog.setContentView(dialogView);
        studymodeDialog.setTitle("学习模式");
        studymodeDialog.show();
        //在对话框里选择学习模式
        setStudyMode(studymodelistview,studymodeDialog);
    }

    //初始化学习模式list
    public void initStudyMode(){
        modeList = new ArrayList<StudyMode>();
        StudyMode sandlockstudy = new StudyMode("计时学习法",R.mipmap.sandclock);
        modeList.add(sandlockstudy);
        StudyMode tomatostudy = new StudyMode("番茄学习法",R.mipmap.tomato);
        modeList.add(tomatostudy);
    }

    //显示番茄学习模式
    public void setTomatoStudy(){
        r1.setBackgroundColor(Color.parseColor("#ff3300"));
        studymode.setText("番茄学习法");
        studymode.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.tomato,0,0,0);
        studyhour.setText("30");
        studyminute.setText("00");
        studyhour.setTextColor(Color.WHITE);
        studyminute.setTextColor(Color.WHITE);
        studytimestate = true;//番茄学习默认时间片（后面改进为可设置时间片）
        /*
        RelativeLayout r1 = (RelativeLayout)findViewById(R.id.studylayout);
        r1.setBackgroundColor(Color.parseColor("#ff3300"));
        studymode.setText("番茄学习法");
        studymode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.tomato,0);
        //studyhour.setTextColor(Color.WHITE);
        //studyminute.setTextColor(Color.WHITE);*/
    }

    //显示计时学习模式
    public void setSandclockStudy(){
        r1.setBackgroundColor(Color.parseColor("#0066ff"));
        studymode.setText("计时学习法");
        studymode.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.sandclock,0,0,0);
        studyhour.setText("00");
        studyminute.setText("00");
        studyhour.setTextColor(Color.WHITE);
        studyminute.setTextColor(Color.WHITE);
        /*RelativeLayout r1 = (RelativeLayout)findViewById(R.id.studylayout);
        r1.setBackgroundColor(Color.parseColor("#0066ff"));
        studymode.setText("计时学习法");
        studymode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.sandclock,0);
        studyhour.setTextColor(Color.parseColor("#FFFFFF"));
        studyminute.setTextColor(Color.parseColor("#FFFFFF"));*/
    }

    //点击选择学习模式
    public void setStudyMode(ListView listView, final Dialog alertDialog){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = MODE_UI;
                        msg.arg1 = position;
                        mhandler.sendMessage(msg);
                    }
                }).start();
                studymodestate = true;
                alertDialog.dismiss();
            }
        });
    }

    public int hour = 0;
    public int minute = 0;

    public void setStudyTime(){
        final AlertDialog settimeDialog = new AlertDialog.Builder(LearnActivity.this).create();
        settimeView = LayoutInflater.from(LearnActivity.this).inflate(R.layout.dialog_settime,null);
        settimeDialog.setView(settimeView);
        settimeDialog.show();
        timePicker = (TimePicker)settimeView.findViewById(R.id.picker);
        timePicker.setIs24HourView(true);
        Button sure = (Button)settimeView.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whichmode == 0){
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                    Message msg = new Message();
                    msg.what = MODE_SETTIME;
                    msg.arg1 = hour;
                    msg.arg2 = minute;
                    mhandler.sendMessage(msg);
                }
                settimeDialog.dismiss();
            }
        });
    }

    private android.os.Handler mhandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MODE_UI:
                    if (msg.arg1 == 0){
                        /*r1.setBackgroundColor(Color.parseColor("#0066ff"));
                        studymode.setText("计时学习法");
                        studymode.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.sandclock,0,0,0);
                        studyhour.setText("00");
                        studyminute.setText("00");
                        studyhour.setTextColor(Color.WHITE);
                        studyminute.setTextColor(Color.WHITE);*/
                        setSandclockStudy();
                        whichmode = 0;
                    }else if (msg.arg1 == 1){
                        /*r1.setBackgroundColor(Color.parseColor("#ff3300"));
                        studymode.setText("番茄学习法");
                        studymode.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.tomato,0,0,0);
                        studyhour.setText("30");
                        studyminute.setText("00");
                        studyhour.setTextColor(Color.WHITE);
                        studyminute.setTextColor(Color.WHITE);
                        studytime = true;//番茄学习默认时间片（后面改进为可设置时间片*/
                        setTomatoStudy();
                        whichmode = 1;
                    }
                    studymodestate = true;
                    break;
                case MODE_SETTIME:
                    String mhour = String.valueOf(msg.arg1);
                    String mminute = String.valueOf(msg.arg2);
                    studyhour.setText(mhour);
                    studyminute.setText(mminute);
                    studytimestate = true;
                    break;
            }
        }
    };

    public void quitStudyView() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LearnActivity.this);
        dialog.setTitle("控制不住寄己");
        dialog.setMessage("确定要放弃学习吗？");
        dialog.setNegativeButton("错了，乖乖回去学习", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        dialog.setPositiveButton("甘愿接受惩罚", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mediaPlayer.start();
                stopMusicDialog();
            }
        });
        dialog.show();
    }

    public void stopMusicDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(LearnActivity.this);
        alert.setTitle("学习");
        alert.setMessage("生命中最美好的两个字");
        alert.setNegativeButton("我错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopPunish();
            }
        });
        alert.setPositiveButton("以后一定认真学习", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopPunish();
            }
        });
        alert.show();
    }

    public void stopPunish(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            isstudystate = false;
            Intent intent = new Intent(LearnActivity.this,LearnActivity.class);
            startActivity(intent);
        }
    }
}
