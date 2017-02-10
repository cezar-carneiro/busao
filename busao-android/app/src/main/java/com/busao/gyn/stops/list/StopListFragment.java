package com.busao.gyn.stops.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.busao.gyn.R;
import com.busao.gyn.data.BusStopDataLoader;
import com.busao.gyn.data.BusStopDataSource;
import com.busao.gyn.data.DataSource;
import com.busao.gyn.stops.BusStop;

import java.util.List;

/**
 * Created by cezar on 03/01/17.
 */
public class StopListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BusStop>> {

    private RecyclerView stopsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ViewSwitcher stopListViewSwitcher;
    private TextView noItemsTextView;

    private StopsRecyclerViewAdapter mAdapter;

    private DataSource dataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bus_stops_list_fragment, null);

        stopListViewSwitcher = (ViewSwitcher) view.findViewById(R.id.stopListViewSwitcher);
        stopsRecyclerView = (RecyclerView) view.findViewById(R.id.stopListRecyclerView);
        noItemsTextView = (TextView) view.findViewById(R.id.noItemsTextView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        stopsRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        stopsRecyclerView.setLayoutManager(mLayoutManager);

        dataSource = new BusStopDataSource(getContext());

        mAdapter = new StopsRecyclerViewAdapter(dataSource);
        stopsRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(BusStopDataLoader.ID, null, this);

        return view;
    }

    private void switchViews(List<BusStop> stops) {
        if (stops != null && stops.size() > 0) {
            if (R.id.stopListRecyclerView == stopListViewSwitcher.getNextView().getId()) {
                stopListViewSwitcher.showNext();
            }
        } else if (R.id.noItemsTextView == stopListViewSwitcher.getNextView().getId()) {
            stopListViewSwitcher.showNext();
        }
    }

    @Override
    public Loader<List<BusStop>> onCreateLoader(int id, Bundle args) {
        Loader loader = new BusStopDataLoader(getActivity(), dataSource, null, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<BusStop>> loader, List<BusStop> data) {
        mAdapter.refresh(data);
        mAdapter.setLoader(loader);
        switchViews(data);
    }

    @Override
    public void onLoaderReset(Loader<List<BusStop>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
