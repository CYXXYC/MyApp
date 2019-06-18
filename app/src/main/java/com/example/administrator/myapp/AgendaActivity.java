package com.example.administrator.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2019/6/2 0002.
 */

public class AgendaActivity extends Activity {

    //进行数据库的操作
    private DataBaseOperator dataBaseOperator;
    //显示agenda的list
    private List<Events>eventsList;
    private AgendaListAdapter agendaListAdapter;
    private ListView agendaListView;

    //显示添加备忘的对话框
    private AlertDialog.Builder alertDialog;
    private View dialogView;

    //备忘的日期，时间，内容

    //两个按钮
    private Button addagenda;
    private Button deleteagenda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        dataBaseOperator = new DataBaseOperator(AgendaActivity.this);

        addagenda = (Button)findViewById(R.id.agenda_add);

        initAgendaListView();//显示agendaList

        addagenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddEventDialog();
            }
        });


    }
    public void setAddEventDialog(){
        alertDialog = new AlertDialog.Builder(AgendaActivity.this);
        dialogView = LayoutInflater.from(AgendaActivity.this).inflate(R.layout.add_newagenda,null);
        alertDialog.setView(dialogView);
        alertDialog.setTitle("添加备忘");
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickAdd();
            }
        });
        alertDialog.show();
    }

    public void initAgendaListView(){
        eventsList = initAgendaList();
        agendaListAdapter = new AgendaListAdapter(AgendaActivity.this,eventsList);
        agendaListView = (ListView)findViewById(R.id.agenda_listview);
        agendaListView.setAdapter(agendaListAdapter);
    }

    public void clickAdd(){
        EditText editDate = (EditText)findViewById(R.id.add_date);
        EditText editTime = (EditText)findViewById(R.id.add_time);
        EditText editThing = (EditText)findViewById(R.id.add_thing);
        //日期和内容是必须的，没有设置时间就默认零点提醒
        if (editThing == null || TextUtils.isEmpty(editThing.getText().toString())){
            Toast.makeText(this,"要提醒你什么呀",Toast.LENGTH_LONG).show();
        }
        else if(editDate == null || TextUtils.isEmpty(editDate.getText().toString())) {
            Toast.makeText(this,"哪天提醒你呀",Toast.LENGTH_LONG).show();
        }else if (dataBaseOperator.timeHaveAdded(editTime.getText().toString())){
            Toast.makeText(this,"该时间段已有安排",Toast.LENGTH_LONG).show();
        }else if (dataBaseOperator.thingHaveAdded(editThing.getText().toString())){
            Toast.makeText(this,"该事件已存在",Toast.LENGTH_LONG).show();
        }else{
            dataBaseOperator.addData(editDate.getText().toString(),editTime.getText().toString(),editThing.getText().toString(),0,0,0,0,0);
            Toast.makeText(this,"成功添加^_^",Toast.LENGTH_LONG).show();
        }
    }

    public List<Events>initAgendaList(){
        List<Events>list = new ArrayList<Events>();
        Cursor cursor = dataBaseOperator.queryData();
        while(cursor.moveToNext()) {
            if (cursor != null && cursor.getCount() >= 0){
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String thing = cursor.getString(cursor.getColumnIndex("thing"));
                list.add(new Events(date,time,thing));
            }
        }

        Collections.sort(eventsList, new Comparator<Events>() {
            @Override
            public int compare(Events o1, Events o2) {
                String date1 = o1.getDate().toString();
                String date2 = o2.getDate().toString();
                String time1 = o1.getTime().toString();
                String time2 = o2.getTime().toString();

                if (date1.compareTo(date2) > 0) return 1;
                else if(date1.compareTo(date2) == 0 && time1.compareTo(time2) > 0) return 1;
                return -1;
            }
        });
        return list;
    }

    class DataBaseOperator{
        private MyDataBaseHelper myDataBaseHelper;

        public DataBaseOperator(Context context){
            super();
            myDataBaseHelper = new MyDataBaseHelper(context);
        }

        public boolean thingHaveAdded(String thing){
            SQLiteDatabase db = myDataBaseHelper.getReadableDatabase();
            Cursor cursor = db.query(myDataBaseHelper.TABLE_NAME,null,"thing = ?",new String[]{thing},null,null,null);
            if (cursor!=null && cursor.getCount() > 0)
                return false;
            else
                return true;
        }

        public boolean timeHaveAdded(String time){
            SQLiteDatabase db = myDataBaseHelper.getReadableDatabase();
            Cursor cursor = db.query(myDataBaseHelper.TABLE_NAME,null,"time = ?",new String[]{time},null,null,null);
            if (cursor!=null && cursor.getCount() > 0)
                return false;
            else
                return true;
        }

        public void addData(String date, String time, String thing, int year, int month, int day, int hour, int minute){
            SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("date",date);
            cv.put("time",time);
            cv.put("thing",thing);
            cv.put("year",year);
            cv.put("month",month);
            cv.put("day",day);
            cv.put("hour",hour);
            cv.put("minute",minute);
            db.insert(myDataBaseHelper.TABLE_NAME,null,cv);
            db.close();
        }

        public void deleteData(String thing){
            SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
            db.delete(myDataBaseHelper.TABLE_NAME,"thing = ?",new String[]{thing});
            db.close();
        }

        public void updateData(String date, String time, String thing, int year, int month, int day, int hour, int minute,String oldthing){
            SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("date",date);
            cv.put("time",time);
            cv.put("thing",thing);
            cv.put("year",year);
            cv.put("month",month);
            cv.put("day",day);
            cv.put("hour",hour);
            cv.put("minute",minute);
            db.update(myDataBaseHelper.TABLE_NAME,cv,"thing = ?",new String[]{oldthing});
            db.close();
        }

        public Cursor queryData(){
            SQLiteDatabase db = myDataBaseHelper.getReadableDatabase();
            Cursor cursor = db.query(myDataBaseHelper.TABLE_NAME,null,null,null,null,null,null);
            return cursor;
        }
    }
}



