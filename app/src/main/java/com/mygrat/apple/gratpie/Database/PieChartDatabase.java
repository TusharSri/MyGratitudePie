package com.mygrat.apple.gratpie.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Database is created and maintained here
 */
@Database(entities = { PieChartData.class }, version = 8 , exportSchema = false)
public abstract class PieChartDatabase extends RoomDatabase {

    private static final String DB_NAME = "piechartdata.db";
    private static volatile PieChartDatabase instance;


    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

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
                DB_NAME)
                .addMigrations(MIGRATION_4_5,MIGRATION_5_6,MIGRATION_6_7,MIGRATION_7_8)
                .build();
    }

    public abstract PieChartDao getPieChartDao();
}
