package com.example.ianalonzo.tourup;

/**
 * Created by Asus on 4/2/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    public void onBindViewHolder(TouristSpotViewHolder holder, final int position) {
        //Binds the data
        final Place place1 = place.get(position);

        holder.textViewTitle.setText(place1.getName());
        holder.textViewDescription.setText(place1.getAddress());
        holder.textViewOpeningHour.setText("OPEN NOW: ");
        try {
            holder.ratingBar.setStepSize(0.1f);
            holder.ratingBar.setRating(Float.parseFloat(place1.getRating()));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        if(place1.getOpen().equals("true")) {
            holder.nowOpen.setText("Yes");
            holder.nowOpen.setTextColor(ContextCompat.getColor(mCtx, R.color.MaterialGreen));
        } else if (place1.getOpen().equals("")){
            holder.nowOpen.setText("Not Available");
        } else {
            holder.nowOpen.setText("No");
            holder.nowOpen.setTextColor(ContextCompat.getColor(mCtx, R.color.MaterialRed));
        }


        Picasso.get().load(place1.getImage()).into(holder.imageView);

        holder.showDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent directions = new Intent(mCtx, DirectionsActivity.class);
                directions.putExtra("latitude", place1.getLat());
                directions.putExtra("longitude", place1.getLng());
                directions.putExtra("placeName", place1.getName());
                mCtx.startActivity(directions);
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

        TextView textViewTitle, textViewDescription, textViewOpeningHour, nowOpen;
        ImageView imageView;
        Button showDirections;
        RatingBar ratingBar;

        public TouristSpotViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewOpeningHour = itemView.findViewById(R.id.textViewOpeningHour);
            nowOpen = itemView.findViewById(R.id.now_open);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            showDirections = itemView.findViewById(R.id.show_direction);
        }
    }

}
