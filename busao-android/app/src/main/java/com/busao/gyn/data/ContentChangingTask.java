package com.busao.gyn.data;


import android.os.AsyncTask;
import android.support.v4.content.Loader;

/**
 * Created by cezar on 23/01/17.
 */
public abstract class ContentChangingTask<P, Q, R> extends AsyncTask<P, Q, R> {

    private Loader<?> mLoader;

    public ContentChangingTask(Loader<?> mLoader) {
        this.mLoader = mLoader;
    }

    @Override
    protected void onPostExecute(R param) {
        mLoader.onContentChanged();
    }

}
