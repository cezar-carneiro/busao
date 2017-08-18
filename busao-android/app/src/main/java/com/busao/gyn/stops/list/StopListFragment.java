package com.busao.gyn.stops.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.components.RecyclerViewEmptySupport;
import com.busao.gyn.data.BusaoDatabase;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.stop.BusStopDataSource;
import com.busao.gyn.events.BusStopChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by cezar on 03/01/17.
 */
public class StopListFragment extends Fragment {

    private StopsRecyclerViewAdapter mAdapter;

    private IBusStopDataSource mDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_stops_list_fragment, null);

        TextView stopListNoFavoritesTextView = (TextView) view.findViewById(R.id.stopListNoFavoritesTextView);
        RecyclerViewEmptySupport stopsRecyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.stopListRecyclerView);
        stopsRecyclerView.setmEmptyView(stopListNoFavoritesTextView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        stopsRecyclerView.setHasFixedSize(false);

        stopsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stopsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDataSource = new BusStopDataSource(BusaoDatabase.getInstance(getContext()).busStopDao());
        mAdapter = new StopsRecyclerViewAdapter(mDataSource);
        stopsRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
        mAdapter.refresh(mDataSource.readFavorites());
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onBusStopChanged(BusStopChanged event){
        mAdapter.refresh(mDataSource.readFavorites());
    }

}
