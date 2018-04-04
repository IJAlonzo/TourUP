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

public class TouristSpotAdapter extends RecyclerView.Adapter<TouristSpotAdapter.TouristSpotViewHolder> {

    private Context mCtx;
    private List<TouristSpot> touristSpotList;

    public TouristSpotAdapter(Context mCtx, List<TouristSpot> touristSpotList) {
        this.mCtx = mCtx;
        this.touristSpotList = touristSpotList;
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
        TouristSpot touristSpot = touristSpotList.get(position);

        holder.textViewTitle.setText(mCtx.getResources().getText(touristSpot.getName()));
        holder.textViewDescription.setText(mCtx.getResources().getText(touristSpot.getDescription()));
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(touristSpot.getImage()));
    }

    @Override
    public int getItemCount() {
        return touristSpotList.size();
    }

    //Constructor for TouristSpotViewHolder
    class TouristSpotViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewDescription;

        public TouristSpotViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }

}
