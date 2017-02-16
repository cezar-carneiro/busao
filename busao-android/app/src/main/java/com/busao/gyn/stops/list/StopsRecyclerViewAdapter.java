package com.busao.gyn.stops.list;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.AbstractDataSource;
import com.busao.gyn.stops.BusStop;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by cezar on 03/01/17.
 */

public class StopsRecyclerViewAdapter extends RecyclerView.Adapter<StopsRecyclerViewAdapter.ViewHolder> {

    private List<BusStop> dataset;
    private AbstractDataSource dataSource;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView stopNumber;
        public TextView streetName;
        public TextView districtName;
        public TextView stopDescription;
        public TextView cityName;
        public TextView linesAvailable;
        public ImageView imageFavorite;
        public ImageView imageMap;

        public ViewHolder(View v) {
            super(v);
            stopNumber = (TextView) v.findViewById(R.id.stop_number);
            streetName = (TextView) v.findViewById(R.id.street_name);
            districtName = (TextView) v.findViewById(R.id.district_name);
            stopDescription = (TextView) v.findViewById(R.id.stop_description);
            cityName = (TextView) v.findViewById(R.id.city_name);
            linesAvailable = (TextView) v.findViewById(R.id.lines_available);
            imageFavorite = (ImageView) v.findViewById(R.id.imageFavorite);
            imageMap = (ImageView) v.findViewById(R.id.imageMap);
        }
    }

    public StopsRecyclerViewAdapter(AbstractDataSource dataSource) {
        this(dataSource, null);
    }

    public StopsRecyclerViewAdapter(AbstractDataSource dataSource, List<BusStop> dataset) {
        this.dataSource = dataSource;
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
        final BusStop stop = dataset.get(position);
        String code = String.valueOf(dataset.get(position).getCode());
        while(code.length() < 4) code = "0" + code;

        holder.stopNumber.setText(code);
        holder.streetName.setText(stop.getAddress());
        holder.districtName.setText(stop.getNeighborhood());

        if(StringUtils.isEmpty(stop.getReference())){
            holder.stopDescription.setText("(Sem descrição disponível)");
            holder.stopDescription.setTypeface(null, Typeface.ITALIC);
        }else{
            holder.stopDescription.setText(stop.getReference());
            holder.stopDescription.setTypeface(null, Typeface.NORMAL);
        }

        holder.cityName.setText(stop.getCity());

        holder.linesAvailable.setText(stop.getLines());

        if(stop.getFavorite() != null && stop.getFavorite()){
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.imageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setFavorite(stop.getFavorite() == null ? true : !stop.getFavorite());
                dataSource.update(stop);
                dataSource.refreshItems();
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


