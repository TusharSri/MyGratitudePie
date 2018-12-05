package com.mygrat.apple.gratpie.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Creation of DAO object is done with this Interface
 */
@Dao
public interface PieChartDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(PieChartData... pieChartData);

    @Query("SELECT * FROM piechartdata WHERE date=:date")
    PieChartData[] getPieChartData(String date);

    @Query("SELECT * FROM piechartdata")
    PieChartData[] getAllPieChartData();

    @Update
    void updatePie(PieChartData... pieChartData);
}
