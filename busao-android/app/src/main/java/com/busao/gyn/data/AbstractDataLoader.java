package com.busao.gyn.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collection;

/**
 * Created by cezar on 23/01/17.
 */
public abstract class AbstractDataLoader<E extends Collection, D extends AbstractDataSource> extends AsyncTaskLoader<E> implements AbstractDataSource.DataSourceObserver{

    protected D mDataSource;

    public AbstractDataLoader(Context context, D dataSource) {
        super(context);
        this.mDataSource = dataSource;
    }

    @Override
    abstract public E loadInBackground();

    @Override
    public void deliverResult(E data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    /**
     * Starts an asynchronous load of the list data. When the result is ready
     * the callbacks will be called on the UI thread. If a previous load has
     * been completed and is still valid the result may be passed to the
     * callbacks immediately.
     * <p>
     * Must be called from the UI thread.
     */
    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded data immediately if available.
        if (mDataSource.cachedItemsAvailable()) {
            deliverResult((E)mDataSource.getCache());
        }

        mDataSource.addContentObserver(this);

        if (takeContentChanged() || !mDataSource.cachedItemsAvailable()) {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mDataSource.removeContentObserver(this);
    }

    @Override
    public void onDataChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }

}
