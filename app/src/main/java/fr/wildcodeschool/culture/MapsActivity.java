package fr.wildcodeschool.culture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.transitionseverywhere.TransitionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION = 4322;
    FloatingActionButton btFavorite, btBurger, btPlaces, btSignOut;
    CoordinatorLayout transitionContainer;
    private boolean mMapInit = false;
    private GoogleMap mMap;
    private LocationManager mLocationManager = null;
    private Location mLocationUser = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkPermission();
        floatingMenu();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocation();
                } else {

                }
                return;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setUserLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, locationListener);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                setUserLocation(location);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void setUserLocation(Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng coordinate = new LatLng(lat, lng);
            mLocationUser = new Location("");
            mLocationUser.setLatitude(lat);
            mLocationUser.setLongitude(lng);

            Singleton singleton = Singleton.getInstance();
            singleton.setLocationUser(mLocationUser);
            float zoomLevel = 16.0f;
            if (mMap != null && !mMapInit) {
                mMapInit = true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoomLevel));
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            mMap.setMyLocationEnabled(true);
            setUserLocation(mLocationUser);
        }
        String json = null;
        try {
            InputStream is = getAssets().open("Toulouse-musees.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONArray root = new JSONArray(json);
            for (int i = 0; i < root.length(); i++) {
                JSONObject rooter = (JSONObject) root.get(i);
                JSONObject fields = rooter.getJSONObject("fields");
                for (int b = 0; b < fields.length(); b++) {
                    JSONArray geolocalisation = (JSONArray) fields.get("geo_point_2d");
                    String name = (String) fields.get("eq_nom_equipement");
                    Double latitude = (Double) geolocalisation.get(0);
                    Double longitude = (Double) geolocalisation.get(1);
                    LatLng museum = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(museum).title(name));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        String url = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=agenda-des-manifestations-culturelles-so-toulouse&facet=type_de_manifestation&refine.type_de_manifestation=Culturelle";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray records = response.getJSONArray("records");
                            ArrayList<Event> events = new ArrayList<>();
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject rec = (JSONObject) records.get(i);
                                JSONObject fields = rec.getJSONObject("fields");
                                String name = (String) fields.get("nom_de_la_manifestation");
                                JSONArray geolocalisation = (JSONArray) fields.get("geo_point");
                                Double latitude = (Double) geolocalisation.get(0);
                                Double longitude = (Double) geolocalisation.get(1);

                                final LatLng event = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)).position(event).title(name));

                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent intent = new Intent(MapsActivity.this, EventsActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void floatingMenu() {

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavorite = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavoriteBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);
        btSignOut = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingSignOut);

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorite.setVisibility(View.VISIBLE);
                    btPlaces.setVisibility(View.VISIBLE);
                    btSignOut.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorite.setVisibility(View.GONE);
                    btPlaces.setVisibility(View.GONE);
                    btSignOut.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFavorites = new Intent(MapsActivity.this, FavoritesActivity.class);
                startActivity(goToFavorites);
            }
        });

        btPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoListMuseum = new Intent(MapsActivity.this, ListMuseum.class);
                startActivity(gotoListMuseum);
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MapsActivity.this, SignIn.class));
            }
        });
    }
}
