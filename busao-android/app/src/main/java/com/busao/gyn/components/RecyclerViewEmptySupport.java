package com.busao.gyn.components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cezar.carneiro on 14/08/2017.
 */

public class RecyclerViewEmptySupport extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if(adapter == null || mEmptyView == null) {
                return;
            }
            if(adapter.getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                RecyclerViewEmptySupport.this.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
            }

        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) {
            adapter.registerAdapterDataObserver(mDataObserver);
        }

        mDataObserver.onChanged();
    }

    public void setmEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }
}