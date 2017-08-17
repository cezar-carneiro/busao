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

import com.busao.gyn.R;
import com.busao.gyn.components.RecyclerViewEmptySupport;
import com.busao.gyn.data.BusLineDataSource;
import com.busao.gyn.data.BusStopDataSource;

/**
 * Created by cezar on 20/03/2017.
 */

public class LinesFragment extends Fragment {

    private LinesRecyclerViewAdapter mAdapter;

    private BusLineDataSource mDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_lines_list_fragment, null);

        TextView stopListNoFavoritesTextView = (TextView) view.findViewById(R.id.stopListNoFavoritesTextView);
        RecyclerViewEmptySupport stopsRecyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.stopListRecyclerView);
        stopsRecyclerView.setmEmptyView(stopListNoFavoritesTextView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        stopsRecyclerView.setHasFixedSize(false);

        stopsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stopsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDataSource = BusLineDataSource.newInstance(getContext());
        mAdapter = new LinesRecyclerViewAdapter(mDataSource);
        stopsRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
