package com.busao.gyn.data;

import android.os.AsyncTask;
import android.support.v4.content.Loader;

/**
 * Created by cezar on 23/01/17.
 */
public abstract class ContentChangingTask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {

    private Loader<?> loader;

    public ContentChangingTask(Loader<?> loader) {
        this.loader=loader;
    }

    @Override
    protected void onPostExecute(T3 param) {
        loader.onContentChanged();
    }

}
