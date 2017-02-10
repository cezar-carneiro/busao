package com.busao.gyn.stops.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.stops.BusStop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar on 03/01/17.
 */

public class StopsRecyclerViewAdapter extends RecyclerView.Adapter<StopsRecyclerViewAdapter.ViewHolder> {

    private List<BusStop> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView stopNumber;
        public TextView streetName;
        public TextView districtName;
        public TextView stopDescription;
        public ImageView imageFavorite;
        public ImageView imageMap;

        public ViewHolder(View v) {
            super(v);
            stopNumber = (TextView) v.findViewById(R.id.stop_number);
            streetName = (TextView) v.findViewById(R.id.street_name);
            districtName = (TextView) v.findViewById(R.id.district_name);
            stopDescription = (TextView) v.findViewById(R.id.stop_description);
            imageFavorite = (ImageView) v.findViewById(R.id.imageFavorite);
            imageMap = (ImageView) v.findViewById(R.id.imageMap);
        }
    }

    public StopsRecyclerViewAdapter() {
        this(null);
    }

    public StopsRecyclerViewAdapter(List<BusStop> dataset) {
        this.dataset = dataset;
    }

    public void refresh(List<BusStop> data){
        if(data == null) {
            return;
        }
        if(dataset == null) {
            dataset = data;
            notifyDataSetChanged();
            return;
        }
        dataset.clear();
        dataset.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        if(dataset == null){
            return;
        }
        dataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public StopsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (dataset == null || dataset.size() == 0) {
            return;
        }
        holder.stopNumber.setText(String.valueOf(dataset.get(position).getCode()));
        holder.streetName.setText(dataset.get(position).getAddress());
        holder.districtName.setText(dataset.get(position).getNeighborhood());
        holder.stopDescription.setText(dataset.get(position).getReference());

        holder.imageFavorite.setOnClickListener(new View.OnClickListener() {
            boolean favorite = false;

            @Override
            public void onClick(View v) {
                favorite = !favorite;
                if(favorite){
                    holder.imageFavorite.setImageResource(R.drawable.ic_favorite);
                }else{
                    holder.imageFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });

        holder.imageMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }
}


