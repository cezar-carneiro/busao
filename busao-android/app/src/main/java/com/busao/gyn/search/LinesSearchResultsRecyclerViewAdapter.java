package com.busao.gyn.search;

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
import com.busao.gyn.util.FormatsUtils;
import com.busao.gyn.util.TextViewUtils;

import java.util.List;

/**
 * Created by cezar.carneiro on 12/05/17.
 */

public class LinesSearchResultsRecyclerViewAdapter extends DefaultRecyclerViewAdapter<BusLine, IBusLineDataSource, LinesSearchResultsRecyclerViewAdapter.ViewHolder> {

    public LinesSearchResultsRecyclerViewAdapter(IBusLineDataSource dataSource) {
        this(dataSource, null);
    }

    public LinesSearchResultsRecyclerViewAdapter(IBusLineDataSource dataSource, List<BusLine> dataset) {
        super(dataSource, dataset);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lineNumberSearchResultTextView;
        public TextView lineDescriptionSearchResultTextView;
        public ImageView favoriteLineSearchResultImageView;

        public ViewHolder(View v) {
            super(v);
            lineNumberSearchResultTextView = (TextView) v.findViewById(R.id.lineNumberSearchResultTextView);
            lineDescriptionSearchResultTextView = (TextView) v.findViewById(R.id.lineDescriptionSearchResultTextView);
            favoriteLineSearchResultImageView = (ImageView) v.findViewById(R.id.favoriteLineSearchResultImageView);
        }
    }

    @Override
    public LinesSearchResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_search_result_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mDataset == null || mDataset.size() == 0) {
            return;
        }
        final BusLine line = mDataset.get(position);
        String code = FormatsUtils.formatBusLine(mDataset.get(position).getCode());

        TextViewUtils.highlight(code, mQuery, holder.lineNumberSearchResultTextView);
        TextViewUtils.highlight(line.getDescription(), mQuery, holder.lineDescriptionSearchResultTextView);

        if(line.isFavorite()){
            holder.favoriteLineSearchResultImageView.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.favoriteLineSearchResultImageView.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.favoriteLineSearchResultImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line.setFavorite(!line.isFavorite());
                if(line.isFavorite()){
                    holder.favoriteLineSearchResultImageView.setImageResource(R.drawable.ic_favorite);
                }else{
                    holder.favoriteLineSearchResultImageView.setImageResource(R.drawable.ic_favorite_border);
                }
                mDataSource.update(line);
            }
        });

    }

}
