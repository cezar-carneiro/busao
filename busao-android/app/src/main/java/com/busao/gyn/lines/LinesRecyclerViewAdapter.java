package com.busao.gyn.lines;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.AbstractDataSource;
import com.busao.gyn.data.BusLine;
import com.busao.gyn.data.BusStop;
import com.busao.gyn.events.MapIconClickEvent;
import com.busao.gyn.schedule.ScheduleActivity;
import com.busao.gyn.util.BusStopUtils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by cezar on 17/08/17.
 */

public class LinesRecyclerViewAdapter extends RecyclerView.Adapter<LinesRecyclerViewAdapter.ViewHolder> {

    private List<BusLine> dataset;
    private AbstractDataSource dataSource;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lineCodeTextView;
        public TextView lineDescriptionTextView;

        public ImageView favoriteLineImageView;
        public ImageView showLineOnMapImageView;

        public ViewHolder(View v) {
            super(v);
            lineCodeTextView = (TextView) v.findViewById(R.id.lineCodeTextView);
            lineDescriptionTextView = (TextView) v.findViewById(R.id.lineDescriptionTextView);

            favoriteLineImageView = (ImageView) v.findViewById(R.id.favoriteLineImageView);
            showLineOnMapImageView = (ImageView) v.findViewById(R.id.showLineOnMapImageView);
        }
    }

    public LinesRecyclerViewAdapter(AbstractDataSource dataSource) {
        this(dataSource, null);
    }

    public LinesRecyclerViewAdapter(AbstractDataSource dataSource, List<BusLine> dataset) {
        this.dataSource = dataSource;
        this.dataset = dataset;
    }

    public void refresh(List<BusLine> data){
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

    public void refreshItem(BusLine item){
        //TODO: we should do something better than this
        for(int i = 0; i < dataset.size(); i++){
            if(dataset.get(i).equals(item.getId())){
                dataset.set(i, item);
                notifyItemChanged(i);
            }
        }
    }

    public void clear(){
        if(dataset == null){
            return;
        }
        dataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public LinesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (dataset == null || dataset.size() == 0) {
            return;
        }
        final BusLine line = dataset.get(position);
        String code = BusStopUtils.formatBusStop(line.getCode());

        holder.lineCodeTextView.setText(code);

        holder.lineDescriptionTextView.setText(line.getDescription());

        holder.favoriteLineImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line.setFavorite(!line.isFavorite());
                if (line.isFavorite()) {
                    holder.favoriteLineImageView.setImageResource(R.drawable.ic_favorite);
                } else {
                    holder.favoriteLineImageView.setImageResource(R.drawable.ic_favorite_border);
                }
                dataSource.update(line);
            }
        });

        holder.showLineOnMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireMapIconClickEvent(line);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    private void fireMapIconClickEvent(BusLine line){
//        EventBus.getDefault().post(new MapIconClickEvent(line));
    }
}


