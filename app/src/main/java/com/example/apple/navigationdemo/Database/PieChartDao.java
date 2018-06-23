package com.example.apple.navigationdemo.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Creation of DAO object is done with this Interface
 */
@Dao
public interface PieChartDao {

    @Insert
    void insert(PieChartData... pieChartData);

    @Query("SELECT * FROM piechartdata WHERE date=:date")
    public PieChartData[] getPieChartData(String date);
}
