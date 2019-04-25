package fr.wildcodeschool.culture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.transitionseverywhere.TransitionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1550;
    FloatingActionButton btFavorite, btBurger, btPlaces, btSignOut;
    CoordinatorLayout transitionContainer;
    private GoogleMap mMap;
    private  GoogleMap eMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkLocationPermission();
        floatingMenu();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        eMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            mMap.setMyLocationEnabled(true);
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
        LatLng toulouse = new LatLng(43.604, 1.443);
        float zoomLevel = 14.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouse, zoomLevel));

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
                                eMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)).position(event).title(name));

                                eMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent intent = new Intent(MapsActivity.this,EventsActivity.class);
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

        btPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMain = new Intent(MapsActivity.this, ListMuseum.class);
                startActivity(gotoMain);

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
