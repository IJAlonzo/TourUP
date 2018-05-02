package com.example.ianalonzo.tourup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TourUniversity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GetNearbyPlacesData.ShowNearbyPlaces {

    private int PROXIMITY_RADIUS = 1000;
    double latitude, longitude;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Location dest = new Location("");
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    volatile boolean stop = false;

    //Database References
    private DatabaseReference databaseReference;
    private DatabaseReference landMarkReference;
    private DatabaseReference latitudeReference;
    private DatabaseReference longitudeReference;
    private DatabaseReference nextReference;
    private DatabaseReference nextLocDatabaseReference;
    private DatabaseReference nextLocNameDatabaseReference;

    private double destinationLat;
    private double destinationLng;
    private String landmarkName;
    private String next;
    private String nextLocName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_university);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        buildGoogleApiClient();

            /*//Instantiate databases
            databaseReference = FirebaseDatabase.getInstance().getReference().child("lnd_1");
            landMarkReference = databaseReference.child("Name");
            landMarkReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    landmarkName = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            nextReference = databaseReference.child("Next");
            nextReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    next = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

        Bundle extras = getIntent().getExtras();
        landmarkName = extras.getString("Name");
        destinationLat = extras.getDouble("Latitude");
        destinationLng = extras.getDouble("Longitude");
        next = extras.getString("Next Location");
        dest.setLatitude(destinationLat);
        dest.setLongitude(destinationLng);


    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission is granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }

                        mMap.setMyLocationEnabled(true);
                    }
                } else {

                    //Permission is denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();

        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        lastLocation = location;

        if (client != null) {

            isInDestination(dest);

        }
    }

    public void isInDestination(Location dest) {

        String distance = String.valueOf(lastLocation.distanceTo(dest));
        Toast.makeText(this, "You are " + distance + " meters away from " + landmarkName, Toast.LENGTH_SHORT).show();

        nextLocDatabaseReference = FirebaseDatabase.getInstance().getReference().child(next);
        nextLocNameDatabaseReference = nextLocDatabaseReference.child("Name");
        nextLocNameDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nextLocName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(lastLocation.distanceTo(dest) <= 70) {

            //for Custom Dialog Box
            LayoutInflater layoutInflaterDialogBox = LayoutInflater.from(getApplicationContext());
            View view = layoutInflaterDialogBox.inflate(R.layout.custom_dialog, null, false);
            final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(TourUniversity.this);
            alertDialogBuilder.setView(view);

            final ImageView imageDialog = (ImageView) view.findViewById(R.id.image_dialog);
            imageDialog.setBackgroundResource(R.drawable.academic_monument);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Go to the next location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Do something
                            Toast.makeText(TourUniversity.this, "Next location is " + nextLocName, Toast.LENGTH_SHORT).show();
                        }
                    })

                    .setNegativeButton("Learn more", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Do something
                        }
                    });


            final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        }
    }
     /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Changes the type of the google map
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }

            LatLng origin = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(origin);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            currentLocationMarker = mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

            final LatLng destination = new LatLng(destinationLat, destinationLng);
            String serverKey = "AIzaSyCwM_MdK7PdouAX8SyfYAO8y0Foz2S9NZU";

            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .unit(Unit.METRIC)
                    .transportMode(TransportMode.WALKING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            Toast.makeText(TourUniversity.this, "Showing direction to " + landmarkName, Toast.LENGTH_SHORT).show();
                            // Do something here
                            if(direction.isOK()) {

                                Route route = direction.getRouteList().get(0);
                                mMap.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                                List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
                                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(TourUniversity.this, stepList, 10, Color.LTGRAY, 3, Color.MAGENTA);
                                for (PolylineOptions polylineOptions : polylineOptionList) {
                                    mMap.addPolyline(polylineOptions);
                                }
                                setCameraWithCoordinationBounds(route);
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                        }
                    });

            //LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }

    }

    protected synchronized void buildGoogleApiClient() {

        client = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

        client.connect();
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

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);

            }

            return false;

        } else {

            return true;

        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void showNearbyPlaces(List<Place> someData) {

        for (int i = 0; i < someData.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            Place googlePlace = someData.get(i);
            double lat = Double.parseDouble(googlePlace.getLat());
            double lng = Double.parseDouble(googlePlace.getLng());
            String placeName = googlePlace.getName();
            String address = googlePlace.getAddress();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + address);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(markerOptions);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }

    @Override
    public void onDestroy() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        stop = true;
        super.onDestroy();
    }
}
