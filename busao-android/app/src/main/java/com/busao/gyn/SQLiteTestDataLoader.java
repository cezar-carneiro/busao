package com.busao.gyn;

import android.content.Context;

import com.busao.gyn.repository.DataSource;
import com.busao.gyn.stops.BusStop;

import java.util.List;


/**
 * Created by cezar on 23/01/17.
 */

public class SQLiteTestDataLoader extends AbstractDataLoader<List> {
    private DataSource mDataSource;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;

    public SQLiteTestDataLoader(Context context, DataSource dataSource, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        super(context);
        mDataSource = dataSource;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
    }

    @Override
    protected List buildList() {
        List testList = mDataSource.read();
        return testList;
    }
    public void insert(BusStop entity) {
        new InsertTask(this).execute(entity);
    }
    public void update(BusStop entity) {
        new UpdateTask(this).execute(entity);
    }
    public void delete(BusStop entity) {
        new DeleteTask(this).execute(entity);
    }
    private class InsertTask extends ContentChangingTask<BusStop, Void, Void> {
        InsertTask(SQLiteTestDataLoader loader) {
            super(loader);
        }
        @Override
        protected Void doInBackground(BusStop... params) {
            mDataSource.create(params[0]);
            return (null);
        }
    }
    private class UpdateTask extends ContentChangingTask<BusStop, Void, Void> {
        UpdateTask(SQLiteTestDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(BusStop... params) {
            mDataSource.update(params[0]);
            return (null);
        }
    }
    private class DeleteTask extends ContentChangingTask<BusStop, Void, Void> {
        DeleteTask(SQLiteTestDataLoader loader) {
            super(loader);
        }
        @Override
        protected Void doInBackground(BusStop... params) {
            mDataSource.delete(params[0]);
            return (null);
        }
    }
}
