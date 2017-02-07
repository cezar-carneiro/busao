package com.busao.gyn.repository;

import java.util.List;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cezar on 23/01/17.
 */
public abstract class DataSource<T> {

    protected SQLiteDatabase mDatabase;

    public DataSource(SQLiteDatabase database) {
        mDatabase = database;
    }

    public abstract boolean create(T entity);
    public abstract T read(Integer id);
    public abstract List<T> read();
    public abstract boolean update(T entity);
    public abstract boolean delete(T entity);
}
