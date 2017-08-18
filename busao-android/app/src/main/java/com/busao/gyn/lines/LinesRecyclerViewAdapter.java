package com.busao.gyn.lines;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.IBusLineDataSource;
import com.busao.gyn.data.line.BusLine;
import com.busao.gyn.util.BusStopUtils;

import java.util.List;

/**
 * Created by cezar on 17/08/17.
 */

public class LinesRecyclerViewAdapter extends RecyclerView.Adapter<LinesRecyclerViewAdapter.ViewHolder> {

    private List<BusLine> mDataset;
    private IBusLineDataSource mDataSource;

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

    public LinesRecyclerViewAdapter(IBusLineDataSource dataSource) {
        this(dataSource, null);
    }

    public LinesRecyclerViewAdapter(IBusLineDataSource dataSource, List<BusLine> dataset) {
        this.mDataSource = dataSource;
        this.mDataset = dataset;
    }

    public void refresh(List<BusLine> data){
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

    public void refreshItem(BusLine item){
        //TODO: we should do something better than this
        for(int i = 0; i < mDataset.size(); i++){
            if(mDataset.get(i).equals(item.getId())){
                mDataset.set(i, item);
                notifyItemChanged(i);
            }
        }
    }

    public void clear(){
        if(mDataset == null){
            return;
        }
        mDataset.clear();
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
        if (mDataset == null || mDataset.size() == 0) {
            return;
        }
        final BusLine line = mDataset.get(position);
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
                mDataSource.update(line);
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
        return mDataset == null ? 0 : mDataset.size();
    }

    private void fireMapIconClickEvent(BusLine line){
//        EventBus.getDefault().post(new MapIconClickEvent(line));
    }
}


