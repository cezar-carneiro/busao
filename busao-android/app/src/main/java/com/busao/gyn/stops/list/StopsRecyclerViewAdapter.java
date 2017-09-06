package com.busao.gyn.stops.list;

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
import com.busao.gyn.data.stop.BusStopWithLines;
import com.busao.gyn.events.StopMapIconClickEvent;
import com.busao.gyn.schedule.ScheduleActivity;
import com.busao.gyn.util.FormatsUtils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by cezar on 03/01/17.
 */

public class StopsRecyclerViewAdapter extends DefaultRecyclerViewAdapter<BusStopWithLines, IBusStopDataSource, StopsRecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView stopNumber;
        public TextView streetName;
        public TextView districtName;
        public TextView stopDescription;
        public TextView cityName;
        public TextView linesAvailable;
        public ImageView imageDelete;
        public ImageView imageMap;

        public ViewHolder(View v) {
            super(v);
            stopNumber = (TextView) v.findViewById(R.id.stop_number);
            streetName = (TextView) v.findViewById(R.id.street_name);
            districtName = (TextView) v.findViewById(R.id.district_name);
            stopDescription = (TextView) v.findViewById(R.id.stop_description);
            cityName = (TextView) v.findViewById(R.id.city_name);
            linesAvailable = (TextView) v.findViewById(R.id.lines_available);
            imageDelete = (ImageView) v.findViewById(R.id.imageDelete);
            imageMap = (ImageView) v.findViewById(R.id.imageMap);
        }
    }

    public StopsRecyclerViewAdapter(IBusStopDataSource dataSource, List<BusStopWithLines> dataset) {
        super(dataSource, dataset);
    }

    @Override
    public StopsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mDataset == null || mDataset.size() == 0) {
            return;
        }
        final BusStopWithLines stop = mDataset.get(position);
        String code = FormatsUtils.formatBusStop(mDataset.get(position).getStop().getCode());

        holder.stopNumber.setText(code);
        holder.streetName.setText(stop.getStop().getAddress());
        holder.districtName.setText(stop.getStop().getNeighborhood());

        if(StringUtils.isEmpty(stop.getStop().getReference())){
//            holder.stopDescription.setVisibility(View.GONE);
            holder.stopDescription.setText("(Sem descrição disponível)");
            holder.stopDescription.setTypeface(null, Typeface.ITALIC);
        }else{
            holder.stopDescription.setText(stop.getStop().getReference());
            holder.stopDescription.setTypeface(null, Typeface.NORMAL);
        }

        holder.cityName.setText(stop.getStop().getCity());

        holder.linesAvailable.setText(stop.getFormatedLines());

//        if(stop.getFavorite() != null && stop.getFavorite()){
//            holder.imageDelete.setImageResource(R.drawable.ic_favorite);
//        }else{
//            holder.imageDelete.setImageResource(R.drawable.ic_favorite_border);
//        }

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.getStop().setFavorite(!stop.getStop().isFavorite());
                mDataSource.update(stop.getStop());
            }
        });

        holder.imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireMapIconClickEvent(stop);
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

    private void fireMapIconClickEvent(BusStopWithLines stop){
        EventBus.getDefault().post(new StopMapIconClickEvent(stop));
    }
}


