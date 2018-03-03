package com.secretsanta.secretsanta;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class EventMActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private boolean marked;
    private LatLng markerPosition;
    private FloatingActionButton btnGoBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_m);

        marked = false; //If is marked previously
        this.btnGoBack =  findViewById(R.id.btnGoBack);
        this.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("GRANTED", "No");
            return;
        }
        locationManager.requestLocationUpdates(String.valueOf(locationManager.getBestProvider(new Criteria(), true)).toString(), 1000, 0, this);
        Log.v("GRANTED", "Yes");


        mMap = googleMap;
        markerPosition = new LatLng(39.693826939133615,2.9987646639347076);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(markerPosition).zoom(12.5f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //mMap.addMarker(new MarkerOptions().position(markerPosition));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title("Secret Santa Here");

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                //Get Marker
                markerPosition = markerOptions.getPosition();

                marked = true;

                //Toast.makeText(getApplicationContext(), markerPosition.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Placed Secret Santa", Toast.LENGTH_SHORT).show();

                EventActivity.urlMap = (getURLMaps(markerPosition));
            }
        });
    }

    private String getURLMaps(LatLng latLng) {
        String s = "http://maps.google.com/maps?q="+latLng.latitude+","+latLng.longitude+"";
        Log.v("URL MAP", s);
        return s;
    }


    @Override
    public void onLocationChanged(Location loc) {

        if (!marked) {
            mMap.clear();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("LONG", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("LAT", latitude);

            markerPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(markerPosition));
            //mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title("Secret Santa Here"));
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}