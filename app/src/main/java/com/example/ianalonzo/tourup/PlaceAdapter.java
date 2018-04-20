package com.example.ianalonzo.tourup;

/**
 * Created by Asus on 4/2/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.TouristSpotViewHolder> {

    private Context mCtx;
    private List<Place> place;

    public PlaceAdapter(Context mCtx, List<Place> placeList) {
        this.mCtx = mCtx;
        this.place = placeList;
    }

    @Override
    public TouristSpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_layout, null);
        return new TouristSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TouristSpotViewHolder holder, int position) {
        //Binds the data
        Place place1 = place.get(position);

        holder.textViewTitle.setText(place1.getName());
        holder.textViewDescription.setText(place1.getAddress());
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(place1.getImage()));
    }

    public void setItems(List<Place> data) {
        this.place.clear();
        this.place.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return place.size();
    }

    //Constructor for TouristSpotViewHolder
    class TouristSpotViewHolder extends RecyclerView.ViewHolder {

        //ImageView imageView;
        TextView textViewTitle, textViewDescription;
        ImageView imageView;

        public TouristSpotViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }

}
