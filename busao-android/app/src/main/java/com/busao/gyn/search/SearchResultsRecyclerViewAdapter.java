package com.busao.gyn.search;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.ScheduleActivity;
import com.busao.gyn.data.AbstractDataSource;
import com.busao.gyn.stops.BusStop;
import com.busao.gyn.util.BusStopUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by cezar.carneiro on 12/05/17.
 */

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {

    private List<BusStop> dataset;
    private AbstractDataSource dataSource;
    private String query;

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

    public SearchResultsRecyclerViewAdapter(AbstractDataSource dataSource) {
        this(dataSource, null);
    }

    public SearchResultsRecyclerViewAdapter(AbstractDataSource dataSource, List<BusStop> dataset) {
        this.dataSource = dataSource;
        this.dataset = dataset;
    }

    public void setQuery(String query){
        this.query = query;
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
    public SearchResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_search_result_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (dataset == null || dataset.size() == 0) {
            return;
        }
        final BusStop stop = dataset.get(position);
        String code = BusStopUtils.formatBusStop(dataset.get(position).getCode());

//        holder.stopNumberSearchResult.setText(code);
        highlight(code, query, holder.stopNumberSearchResult);
//        holder.streetNameSearchResult.setText(stop.getAddress());
        highlight(stop.getAddress(), query, holder.streetNameSearchResult);
        holder.districtNameSearchResult.setText(stop.getNeighborhood());

        if(StringUtils.isEmpty(stop.getReference())){
            holder.stopDescriptionSearchResult.setVisibility(View.GONE);
//            holder.stopDescription.setText("(Sem descrição disponível)");
//            holder.stopDescription.setTypeface(null, Typeface.ITALIC);
        }else{
            holder.stopDescriptionSearchResult.setTypeface(null, Typeface.NORMAL);
//            holder.stopDescriptionSearchResult.setText(stop.getReference());
            highlight(stop.getReference(), query, holder.stopDescriptionSearchResult);
        }

        holder.cityNameSearchResult.setText(stop.getCity());

        if(stop.getFavorite() != null && stop.getFavorite()){
            holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.imageFavoriteSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setFavorite(stop.getFavorite() == null ? true : !stop.getFavorite());
                if(stop.getFavorite() != null && stop.getFavorite()){
                    holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite);
                }else{
                    holder.imageFavoriteSearchResult.setImageResource(R.drawable.ic_favorite_border);
                }
                dataSource.update(stop);
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
        return dataset == null ? 0 : dataset.size();
    }

    private void highlight(String text, String span, TextView textView){
        if (StringUtils.isEmpty(span)) {
            textView.setText(text);
            return;
        }
        int start = text.toLowerCase().indexOf(span.toLowerCase());
        if (start < 0) {
            textView.setText(text);
            return;
        }
        int end = start + span.length();

        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new BackgroundColorSpan(0xFFFF9100), start, end, 0);
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, 0);
        textView.setText(spannable);
    }

}


