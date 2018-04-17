package com.example.ianalonzo.tourup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FindHotelActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TouristSpotAdapter adapter;

    List<TouristSpot> touristSpotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hotel);

        touristSpotList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_hotel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        touristSpotList.add(
                new TouristSpot(
                        R.string.upgate_title,
                        R.string.lorem,
                        R.drawable.upgate
                )
        );

        touristSpotList.add(
                new TouristSpot(
                        R.string.cpark_title,
                        R.string.lorem,
                        R.drawable.carabaopark
                )
        );

        touristSpotList.add(
                new TouristSpot(
                        R.string.upgate_title,
                        R.string.lorem,
                        R.drawable.upgate
                )
        );

        touristSpotList.add(
                new TouristSpot(
                        R.string.upgate_title,
                        R.string.lorem,
                        R.drawable.upgate
                )
        );

        adapter = new TouristSpotAdapter(this, touristSpotList);
        recyclerView.setAdapter(adapter);

    }

    private String getUrl(String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("query="+ nearbyPlace);
        //googlePlacesUrl.append("location=" + latitude + "," + longitude);
        //googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        //googlePlacesUrl.append("&type=" + nearbyPlace);
        //googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCwM_MdK7PdouAX8SyfYAO8y0Foz2S9NZU");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

}
