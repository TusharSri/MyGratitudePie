package com.example.apple.navigationdemo.Database;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * This is the model class for Database object
 */
@Entity(primaryKeys={"date", "counter"})
public class PieChartData {
    @NonNull
    private String date;
    @NonNull
    private int counter;
    private String momentDesc;
    private String attachedUrl;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMomentDesc() {
        return momentDesc;
    }

    public void setMomentDesc(String momentDesc) {
        this.momentDesc = momentDesc;
    }

    public String getAttachedUrl() {
        return attachedUrl;
    }

    public void setAttachedUrl(String attachedUrl) {
        this.attachedUrl = attachedUrl;
    }

    public PieChartData(String date, int counter, String momentDesc, String attachedUrl) {
        this.date = date;
        this.counter = counter;
        this.momentDesc = momentDesc;
        this.attachedUrl = attachedUrl;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
