package com.example.ianalonzo.tourup;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus on 4/5/2018.
 */

public class GetNearbyPlacesData extends AsyncTask<String, String, String> {

    String googlePlacesData;
    ShowNearbyPlaces nearbyPlaces;
    GoogleMap mMap;
    String url;

    public GetNearbyPlacesData(ShowNearbyPlaces nearbyPlaces) {
        this.nearbyPlaces = nearbyPlaces;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");

        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<Place> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        nearbyPlaces.showNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    interface ShowNearbyPlaces {
        void showNearbyPlaces (List<Place> someData);
    }
}
