package com.busao.gyn.search;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.DefaultRecyclerViewAdapter;
import com.busao.gyn.R;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.stop.BusStop;
import com.busao.gyn.schedule.ScheduleActivity;
import com.busao.gyn.util.FormatsUtils;
import com.busao.gyn.util.TextViewUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by cezar.carneiro on 12/05/17.
 */

public class StopsSearchResultsRecyclerViewAdapter extends DefaultRecyclerViewAdapter<BusStop, IBusStopDataSource, StopsSearchResultsRecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView stopNumberSearchResult;
        public TextView streetNameSearchResult;
        public TextView districtNameSearchResult;
        public TextView stopDescriptionSearchResult;
        public TextView cityNameSearchResult;
        public ImageView imageFavoriteSearchResult;

        public ViewHolder(View v) {
            super(v);
            stopNumberSearchResult = (TextView) v.findViewById(R.id.stopNumberSearchResult);
            streetNameSearchResult = (TextView) v.findViewById(R.id.streetNameSearchResult);
            districtNameSearchResult = (TextView) v.findViewById(R.id.districtNameSearchResult);
            stopDescriptionSearchResult = (TextView) v.findViewById(R.id.stopDescriptionSearchResult);
            cityNameSearchResult = (TextView) v.findViewById(R.id.cityNameSearchResult);
            imageFavoriteSearchResult = (ImageView) v.findViewById(R.id.imageFavoriteSearchResult);
        }
    }

    public StopsSearchResultsRecyclerViewAdapter(IBusStopDataSource dataSource) {
        super(dataSource, null);
    }

    @Override
    public StopsSearchResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_search_result_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mDataset == null || mDataset.size() == 0) {
            return;
        }
        final BusStop stop = mDataset.get(position);
        String code = FormatsUtils.formatBusStop(mDataset.get(position).getCode());

        TextViewUtils.highlight(code, mQuery, holder.stopNumberSearchResult);
        TextViewUtils.highlight(stop.getAddress(), mQuery, holder.streetNameSearchResult);
        holder.districtNameSearchResult.setText(stop.getNeighborhood());

        if(StringUtils.isEmpty(stop.getReference())){
            holder.stopDescriptionSearchResult.setVisibility(View.GONE);
        }else{
            holder.stopDescriptionSearchResult.setTypeface(null, Typeface.NORMAL);
            TextViewUtils.highlight(stop.getReference(), mQuery, holder.stopDescriptionSearchResult);
        }

        holder.cityNameSearchResult.setText(stop.getCity());

        if(stop.isFavorite()){
            holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.imageFavoriteSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setFavorite(!stop.isFavorite());
                if(stop.isFavorite()){
                    holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite);
                }else{
                    holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite_border);
                }
                mDataSource.update(stop);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ScheduleActivity.class);
                intent.putExtra("stop", stop);
                v.getContext().startActivity(intent);
            }
        });

    }

}


