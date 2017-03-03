package com.busao.gyn.data;

import android.content.Context;

import com.busao.gyn.stops.BusStop;

import java.util.List;


/**
 * Created by cezar on 23/01/17.
 */

public class BusStopDataLoader extends AbstractDataLoader<List<BusStop>, BusStopDataSource> {

    public static final int ID = 1;

    public BusStopDataLoader(Context context, BusStopDataSource dataSource) {
        super(context, dataSource);
    }

    @Override
    public List<BusStop> loadInBackground() {
        return mDataSource.read();
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
        InsertTask(BusStopDataLoader loader) {
            super(loader);
        }
        @Override
        protected Void doInBackground(BusStop... params) {
            mDataSource.create(params[0]);
            return (null);
        }
    }
    private class UpdateTask extends ContentChangingTask<BusStop, Void, Void> {
        UpdateTask(BusStopDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(BusStop... params) {
            mDataSource.update(params[0]);
            return (null);
        }
    }
    private class DeleteTask extends ContentChangingTask<BusStop, Void, Void> {
        DeleteTask(BusStopDataLoader loader) {
            super(loader);
        }
        @Override
        protected Void doInBackground(BusStop... params) {
            mDataSource.delete(params[0]);
            return (null);
        }
    }
}
