package com.example.administrator.myapp;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2019/5/13 0013.
 */

public class StudyMode {
    private String modetext;
    private int imageid;


    public StudyMode(String modetext,int imageid){
        this.imageid = imageid;
        this.modetext = modetext;
    }

    public String getModeText(){
        return modetext;
    }

    public int getImageID(){
        return imageid;
    }

    public void setModetext(String modetext){
        this.modetext = modetext;
    }

    public void setImageid(int imageid){
        this.imageid = imageid;
    }

}
