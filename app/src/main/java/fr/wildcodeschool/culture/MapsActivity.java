package fr.wildcodeschool.culture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.transitionseverywhere.TransitionManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    FloatingActionButton btFavoris, btBurger, btPlaces;
    CoordinatorLayout transitionContainer;

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1550;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkLocationPermission();
        menuBurger();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted : ask for it
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Permission granted
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    // Permission denied
                }
                break;
            }
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        else{
            mMap.setMyLocationEnabled(true);
        }

        LatLng toulouse = new LatLng(43.604, 1.443);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouse, zoomLevel));
    }

    public void menuBurger(){

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavoris = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavorisBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavoris.setVisibility(View.VISIBLE);
                    btPlaces.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavoris.setVisibility(View.GONE);
                    btPlaces.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });
    }
}
