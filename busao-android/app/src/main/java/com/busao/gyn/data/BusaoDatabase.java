package com.busao.gyn.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by cezar.carneiro on 17/08/2017.
 */

@Database(entities = {BusStop.class, BusLine.class, LineStop.class}, version = 1)
public abstract class BusaoDatabase extends RoomDatabase {

    private static BusaoDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract BusStopDao busStopDao();

    public abstract BusLineDao busLineDao();

    public static BusaoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BusaoDatabase.class, "database.db").build();
            }
            return INSTANCE;
        }
    }

}
