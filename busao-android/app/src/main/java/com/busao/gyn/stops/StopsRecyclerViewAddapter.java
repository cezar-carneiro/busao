package com.busao.gyn.stops;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;

/**
 * Created by cezar on 03/01/17.
 */

public class StopsRecyclerViewAddapter extends RecyclerView.Adapter<StopsRecyclerViewAddapter.ViewHolder> {

    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView stopNumber;
        public TextView streetName;
        public TextView districtName;
        public TextView stopDescription;
        public ImageView imageFavorite;

        public ViewHolder(View v) {
            super(v);
            stopNumber = (TextView) v.findViewById(R.id.stop_number);
            imageFavorite = (ImageView) v.findViewById(R.id.imageFavorite);

        }
    }

    public StopsRecyclerViewAddapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StopsRecyclerViewAddapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.stopNumber.setText(mDataset[position]);
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

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


