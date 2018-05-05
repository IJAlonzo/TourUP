package com.example.ianalonzo.tourup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FindRestaurantActivity extends AppCompatActivity implements GetNearbyPlacesData.ShowNearbyPlaces {

    RecyclerView recyclerView;
    PlaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_restaurant);

        recyclerView = (RecyclerView) findViewById(R.id.rv_restaurant);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlaceAdapter(this, new ArrayList<Place>());
        recyclerView.setAdapter(adapter);

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(FindRestaurantActivity.this);
        getNearbyPlacesData.execute(getUrl("restaurants+in+UPLB"));
    }

    private String getUrl(String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("query="+ nearbyPlace);
        //googlePlacesUrl.append("location=" + latitude + "," + longitude);
        //googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        //googlePlacesUrl.append("&type=" + nearbyPlace);
        //googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAedB_XHL2NJzYM9zsE72xFEiSlOTHMk28");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void showNearbyPlaces(List<Place> someData) {
        Log.d("FindRestaurantActivity", "HARO " + someData.size());
        adapter.setItems(someData);
    }
}
