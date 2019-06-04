package com.example.administrator.myapp;

public class Events {
    private String date;
    private String time;
    private String thing;

    public Events(String date, String time,String thing){
        this.date = date;
        this.time = time;
        this.thing = thing;
    }

    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}
    public String getTime(){return time;}
    public void setTime(String time){this.time = time;}
    public String getThing(){return thing;}
    public void setThing(String thing){this.thing = thing;}

}
