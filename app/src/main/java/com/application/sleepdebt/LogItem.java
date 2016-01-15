package com.application.sleepdebt;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zomadmin on 04/12/15.
 */
public class LogItem implements Serializable{
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setFirstLine(String firstLine) {
        this.firstLine = firstLine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSecondLine(String secondLine) {
        this.secondLine = secondLine;
    }

    public int getId() {
        return id;
    }

    public int id;
    public String firstLine;
    public String secondLine;
    public String currentTime;


    public LogItem(){

    }
    public LogItem(String first, String second){
        firstLine= first;
        secondLine = second;
        DateFormat df = new SimpleDateFormat("HH:mm\nEEE\ndd/MMM/yy", Locale.US);
        Date dateobj = new Date();
        currentTime = df.format(dateobj);
    }
}
