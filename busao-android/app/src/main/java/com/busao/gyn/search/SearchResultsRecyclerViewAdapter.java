package com.busao.gyn.search;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.stop.BusStopWithLines;
import com.busao.gyn.schedule.ScheduleActivity;
import com.busao.gyn.util.BusStopUtils;
import com.busao.gyn.util.TextViewUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by cezar.carneiro on 12/05/17.
 */

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {

    private List<BusStopWithLines> mDataset;
    private IBusStopDataSource mDataSource;
    private String mQuery;

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

    public SearchResultsRecyclerViewAdapter(IBusStopDataSource dataSource) {
        this(dataSource, null);
    }

    public SearchResultsRecyclerViewAdapter(IBusStopDataSource dataSource, List<BusStopWithLines> dataset) {
        this.mDataSource = dataSource;
        this.mDataset = dataset;
    }

    public void setQuery(String query){
        this.mQuery = query;
    }

    public void refresh(List<BusStopWithLines> data){
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

    public void clear(){
        if(mDataset == null){
            return;
        }
        mDataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public SearchResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_search_result_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mDataset == null || mDataset.size() == 0) {
            return;
        }
        final BusStopWithLines stop = mDataset.get(position);
        String code = BusStopUtils.formatBusStop(mDataset.get(position).getStop().getCode());

        TextViewUtils.highlight(code, mQuery, holder.stopNumberSearchResult);
        TextViewUtils.highlight(stop.getStop().getAddress(), mQuery, holder.streetNameSearchResult);
        holder.districtNameSearchResult.setText(stop.getStop().getNeighborhood());

        if(StringUtils.isEmpty(stop.getStop().getReference())){
            holder.stopDescriptionSearchResult.setVisibility(View.GONE);
        }else{
            holder.stopDescriptionSearchResult.setTypeface(null, Typeface.NORMAL);
            TextViewUtils.highlight(stop.getStop().getReference(), mQuery, holder.stopDescriptionSearchResult);
        }

        holder.cityNameSearchResult.setText(stop.getStop().getCity());

        if(stop.getStop().isFavorite()){
            holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.imageFavoriteSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.getStop().setFavorite(!stop.getStop().isFavorite());
                if(stop.getStop().isFavorite()){
                    holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite);
                }else{
                    holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite_border);
                }
                mDataSource.update(stop.getStop());
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

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

}


