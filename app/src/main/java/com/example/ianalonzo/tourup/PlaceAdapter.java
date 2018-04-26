package com.example.ianalonzo.tourup;

/**
 * Created by Asus on 4/2/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
        View view = inflater.inflate(R.layout.card_layout, parent, false);
        return new TouristSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TouristSpotViewHolder holder, int position) {
        //Binds the data
        final Place place1 = place.get(position);

        holder.textViewTitle.setText(place1.getName());
        holder.textViewDescription.setText(place1.getAddress());
        holder.textViewOpeningHour.setVisibility(View.GONE);
        Picasso.get().load(place1.getImage()).into(holder.imageView);

        holder.showDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCtx, "You are going to " + place1.getName(), Toast.LENGTH_SHORT).show();
            }
        });

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
        TextView textViewTitle, textViewDescription, textViewOpeningHour;
        ImageView imageView;
        Button showDirections;

        public TouristSpotViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewOpeningHour = itemView.findViewById(R.id.textViewOpeningHour);
            showDirections = itemView.findViewById(R.id.show_direction);
        }
    }

}
