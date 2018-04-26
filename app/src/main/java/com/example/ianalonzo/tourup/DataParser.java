package com.example.ianalonzo.tourup;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus on 4/5/2018.
 */

public class DataParser {

    public List<Place> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<Place> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<Place> placesList = new ArrayList<>();

        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placesList.add(getPlace((JSONObject) jsonArray.get(i)));
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private Place getPlace(JSONObject googlePlaceJson) {
        Place googlePlace = new Place();
        String placeName = "-NA-";
        String address = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String photoReference = "-NA-";

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("formatted_address")) {
                address = googlePlaceJson.getString("formatted_address");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");

            if (!googlePlaceJson.isNull("photos")) {
                JSONArray photos = googlePlaceJson.getJSONArray("photos");
                if (photos.length()>0) {
                    photoReference = ((JSONObject)photos.get(0)).getString("photo_reference");
                }
            }

            StringBuilder imageUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
            imageUrl.append("maxwidth=400");
            imageUrl.append("&photoreference=" + photoReference);
            imageUrl.append("&key=" + "AIzaSyCwM_MdK7PdouAX8SyfYAO8y0Foz2S9NZU");

            googlePlace = new Place(placeName, address, latitude, longitude, reference, imageUrl.toString());

            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlace;
    }

}
