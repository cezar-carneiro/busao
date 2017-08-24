package com.busao.gyn;

import android.support.v7.widget.RecyclerView;

import com.busao.gyn.data.IDataSource;

import java.util.List;

/**
 * Created by cezar.carneiro on 24/08/2017.
 */

abstract public class DefaultRecyclerViewAdapter<T, D extends IDataSource, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected List<T> mDataset;
    protected D mDataSource;
    protected String mQuery;

    public DefaultRecyclerViewAdapter(D dataSource, List<T> dataset) {
        this.mDataSource = dataSource;
        this.mDataset = dataset;
    }

    final public void setQuery(String query){
        this.mQuery = query;
    }

    final public void refresh(List<T> data){
        if(data == null) {
            return;
        }
        if(mDataset == null) {
            mDataset = data;
            notifyDataSetChanged();
            return;
        }
        mDataset.clear();
        mDataset.addAll(data);
        notifyDataSetChanged();
    }

    final public void refreshItem(T item){
        //TODO: we should something better than this
        for(int i = 0; i < mDataset.size(); i++){
            if(mDataset.get(i).equals(item)){
                mDataset.set(i, item);
                notifyItemChanged(i);
            }
        }
    }

    final public void clear(){
        if(mDataset == null){
            return;
        }
        mDataset.clear();
        notifyDataSetChanged();
    }

    @Override
    final public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

}
