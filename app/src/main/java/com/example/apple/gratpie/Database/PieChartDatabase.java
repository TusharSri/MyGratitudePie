package com.example.apple.gratpie.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Database is created and maintained here
 */
@Database(entities = { PieChartData.class }, version = 1 , exportSchema = false)
public abstract class PieChartDatabase extends RoomDatabase {

    private static final String DB_NAME = "piechartdata.db";
    private static volatile PieChartDatabase instance;

    public static synchronized PieChartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static PieChartDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                PieChartDatabase.class,
                DB_NAME).build();
    }

    public abstract PieChartDao getPieChartDao();
}
