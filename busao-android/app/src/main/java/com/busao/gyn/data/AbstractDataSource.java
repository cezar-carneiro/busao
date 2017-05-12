package com.busao.gyn.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cezar on 23/01/17.
 */
public abstract class AbstractDataSource<T> {

    protected SQLiteDatabase mDatabase;
    protected DataBaseHelper mDatabaseHelper;

    protected AbstractDataSource(Context context) {
        this.mDatabaseHelper = new DataBaseHelper(context);
        this.mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close(){
        this.mDatabaseHelper.close();
        this.mDatabase.close();
    }

    public boolean create(T entity){
        return doCreate(entity);
    }

    protected abstract boolean doCreate(T entity);

    public T read(Integer id){
        return doRead(id);
    }

    protected abstract T doRead(Integer id);

    public List<T> read(){
        return doRead();
    }

    protected abstract List<T> doRead();

    public boolean update(T entity){
        return doUpdate(entity);
    }

    protected abstract boolean doUpdate(T entity);

    public boolean delete(T entity){
        return doDelete(entity);
    }

    protected abstract boolean doDelete(T entity);

}
