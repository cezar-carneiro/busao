package com.busao.gyn.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.busao.gyn.data.line.BusLine;
import com.busao.gyn.data.line.BusLineDao;
import com.busao.gyn.data.stop.BusStop;
import com.busao.gyn.data.stop.BusStopDao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by cezar.carneiro on 17/08/2017.
 */

@Database(entities = {BusStop.class, BusLine.class, LineStop.class}, version = 1)
public abstract class BusaoDatabase extends RoomDatabase {

    private static BusaoDatabase INSTANCE;

    private static String DB_NAME ="database.sql";

    private static final Object sLock = new Object();

    public abstract BusStopDao busStopDao();

    public abstract BusLineDao busLineDao();

    public static BusaoDatabase getInstance(final Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BusaoDatabase.class, "database.db").addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        try {
                            populateDataBase(context, db);
                        } catch (IOException e) {
                            Log.e(null, null, e);
                        }
                    }
                }).allowMainThreadQueries().build();
            }
            return INSTANCE;
        }
    }

    private static void populateDataBase(Context context, SupportSQLiteDatabase db) throws IOException {
        Scanner read = new Scanner (context.getAssets().open(DB_NAME));
        read.useDelimiter(";");

        while (read.hasNext()){
            db.execSQL(read.next());
        }
        read.close();
    }

}
