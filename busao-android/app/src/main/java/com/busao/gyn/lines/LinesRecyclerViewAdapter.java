package com.busao.gyn.lines;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.DefaultRecyclerViewAdapter;
import com.busao.gyn.R;
import com.busao.gyn.data.IBusLineDataSource;
import com.busao.gyn.data.line.BusLine;
import com.busao.gyn.data.line.BusLinesWithStops;
import com.busao.gyn.util.FormatsUtils;

import java.util.List;

/**
 * Created by cezar on 17/08/17.
 */

public class LinesRecyclerViewAdapter extends DefaultRecyclerViewAdapter<BusLinesWithStops, IBusLineDataSource, LinesRecyclerViewAdapter.ViewHolder> {


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

    public LinesRecyclerViewAdapter(IBusLineDataSource dataSource, List<BusLinesWithStops> dataset) {
        super(dataSource, dataset);
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
        final BusLinesWithStops line = mDataset.get(position);
        String code = FormatsUtils.formatBusLine(line.getLine().getCode());

        holder.lineCodeTextView.setText(code);

        holder.lineDescriptionTextView.setText(line.getLine().getDescription());

        if(line.getLine().isFavorite()){
            holder.favoriteLineImageView.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.favoriteLineImageView.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.favoriteLineImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line.getLine().setFavorite(!line.getLine().isFavorite());
                if (line.getLine().isFavorite()) {
                    holder.favoriteLineImageView.setImageResource(R.drawable.ic_favorite);
                } else {
                    holder.favoriteLineImageView.setImageResource(R.drawable.ic_favorite_border);
                }
                mDataSource.update(line.getLine());
            }
        });

        holder.showLineOnMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireMapIconClickEvent(line.getLine());
            }
        });

    }

    private void fireMapIconClickEvent(BusLine line){
//        EventBus.getDefault().post(new MapIconClickEvent(line));
    }
}


