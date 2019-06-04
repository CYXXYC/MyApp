package com.example.administrator.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2019/6/2 0002.
 */

public class AgendaListAdapter extends BaseAdapter {

    private List<Events>mlist;
    private Context mcontext;

    public AgendaListAdapter(Context context,List<Events>list){
        mcontext = context;
        mlist = list;
    }

    @Override
    public int getCount() {
        if (mlist == null)  return 0;
        else return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        if (mlist == null)  return 0;
        else return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mlist == null)  return 0;
        else return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Events events = (Events) getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(mcontext).inflate(R.layout.item_agenda,null);
            viewHolder = new ViewHolder();
            viewHolder.data = (TextView)convertView.findViewById(R.id.event_date);
            viewHolder.time = (TextView)convertView.findViewById(R.id.event_time);
            viewHolder.thing = (TextView)convertView.findViewById(R.id.event_thing);
            convertView.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.data.setText(events.getDate());
        viewHolder.time.setText(events.getTime());
        viewHolder.thing.setText(events.getThing());

        return view;
    }

    class ViewHolder{
        TextView data;
        TextView time;
        TextView thing;
    }
}
