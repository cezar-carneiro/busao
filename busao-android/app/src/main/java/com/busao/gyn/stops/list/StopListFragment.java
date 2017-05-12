package com.busao.gyn.stops.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.busao.gyn.R;
import com.busao.gyn.data.BusStopDataSource;
import com.busao.gyn.events.BusStopChanged;
import com.busao.gyn.stops.BusStop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by cezar on 03/01/17.
 */
public class StopListFragment extends Fragment {

    private ViewSwitcher mStopListViewSwitcher;

    private StopsRecyclerViewAdapter mAdapter;

    private BusStopDataSource mDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_stops_list_fragment, null);

        mStopListViewSwitcher = (ViewSwitcher) view.findViewById(R.id.stopListViewSwitcher);
        RecyclerView stopsRecyclerView = (RecyclerView) view.findViewById(R.id.stopListRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        stopsRecyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        stopsRecyclerView.setLayoutManager(layoutManager);
        stopsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDataSource = BusStopDataSource.newInstance(getContext());
        List<BusStop> stops = mDataSource.read();
        mAdapter = new StopsRecyclerViewAdapter(mDataSource, stops);
        stopsRecyclerView.setAdapter(mAdapter);
        switchViews(stops);
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mDataSource.destroyInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onBusStopChanged(BusStopChanged event){
        mAdapter.refresh(mDataSource.read());
    }

    private void switchViews(List<BusStop> stops) {
        if (stops != null && stops.size() > 0) {
            if (R.id.stopListRecyclerView == mStopListViewSwitcher.getNextView().getId()) {
                mStopListViewSwitcher.showNext();
            }
        } else if (R.id.noItemsTextView == mStopListViewSwitcher.getNextView().getId()) {
            mStopListViewSwitcher.showNext();
        }
    }
}
