package com.busao.gyn.lines;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.busao.gyn.DefaultRecyclerViewAdapter;
import com.busao.gyn.R;
import com.busao.gyn.components.RecyclerViewEmptySupport;
import com.busao.gyn.data.BusaoDatabase;
import com.busao.gyn.data.IBusLineDataSource;
import com.busao.gyn.data.line.BusLineDataSource;
import com.busao.gyn.events.BusLineChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by cezar on 20/03/2017.
 */

public class LinesFragment extends Fragment {

    private DefaultRecyclerViewAdapter mAdapter;

    private IBusLineDataSource mDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_lines_list_fragment, null);

        TextView stopListNoFavoritesTextView = (TextView) view.findViewById(R.id.linesListNoFavoritesTextView);
        RecyclerViewEmptySupport stopsRecyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.linesListRecyclerView);
        stopsRecyclerView.setEmptyView(stopListNoFavoritesTextView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        stopsRecyclerView.setHasFixedSize(false);

        stopsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stopsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDataSource = new BusLineDataSource(BusaoDatabase.get(getContext()).busLineDao());
        mAdapter = new LinesRecyclerViewAdapter(mDataSource,null);
        stopsRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        mAdapter.refresh(mDataSource.readFavorites());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onBusLineChanged(BusLineChanged event){
        mAdapter.refresh(mDataSource.readFavorites());
    }
}
