package com.example.administrator.myapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2019/5/13 0013.
 */

public class StudyModeAdapter extends ArrayAdapter<StudyMode> {
    private int resourceid;

    public StudyModeAdapter(Context context,int resource,List<StudyMode> objects) {
        super(context, resource, objects);
        resourceid = resource;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        StudyMode studyMode = getItem(position);
        View view = LayoutInflater.from(getContext() ).inflate(resourceid,parent,false);
        TextView modeText = (TextView)view.findViewById(R.id.modetext);
        ImageView modeImage = (ImageView)view.findViewById(R.id.modeimage);
        modeText.setText(studyMode.getModeText());
        modeImage.setImageResource(studyMode.getImageID());

        return view;
    }
}
