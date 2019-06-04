package com.example.administrator.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2019/6/2 0002.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="Events.db";
    public static final String TABLE_NAME = "Events";
    public static final int DB_VERSION = 1;
    public static String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME
            + " (_id INTEGER PRIMARY KEY, date TEXT, time TEXT, thing TEXT , year INTEGER , month INTEGER , day INTEGER , hour INTEGER , minute INTEGER)";

    private Context mcontext;

    public MyDataBaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Events");
        onCreate(db);
    }
}
