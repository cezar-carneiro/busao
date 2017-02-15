package com.busao.gyn.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cezar on 23/01/17.
 */
public abstract class AbstractDataSource<T> {

    public interface DataSourceObserver {
        void onDataChanged();
    }

    private List<DataSourceObserver> mObservers = new ArrayList<DataSourceObserver>();

    private Map<Integer, T> mCachedItems;

    private boolean mCacheIsDirty;

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

    public void addContentObserver(DataSourceObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(DataSourceObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (DataSourceObserver observer : mObservers) {
            observer.onDataChanged();
        }
    }

    public void refreshItems() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    public boolean cachedItemsAvailable() {
        return mCachedItems != null && !mCacheIsDirty;
    }

    public Collection<T> getCache() {
        return mCachedItems == null ? null : mCachedItems.values();
    }

    public T getCachedItem(String id) {
        return mCachedItems.get(id);
    }

    public abstract boolean create(T entity);
    public abstract T read(Integer id);
    public abstract List<T> read();
    public abstract boolean update(T entity);
    public abstract boolean delete(T entity);
}
