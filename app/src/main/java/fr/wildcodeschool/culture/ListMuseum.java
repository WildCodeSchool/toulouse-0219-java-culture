package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import static fr.wildcodeschool.culture.Museum.extractJson;

public class ListMuseum extends AppCompatActivity {
    private static boolean dropOff = true;
    private static int zoom = 15;
    FloatingActionButton btFavorite, btBurger, btPlaces, btProfile, btSignOut;
    Button btBack;
    CoordinatorLayout transitionContainer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_museum);
        floatingMenu();

        Singleton singleton = Singleton.getInstance();
        Location coord = singleton.getLocationUser();

        extractJson(ListMuseum.this, coord, dropOff, zoom, new Museum.MuseumListener() {
            @Override
            public void onResult(ArrayList<Museum> museums) {
                ListView listMenu = findViewById(R.id.listView);
                ListMuseumAdapter adapter = new ListMuseumAdapter(ListMuseum.this, museums);
                listMenu.setAdapter(adapter);
            }
        });
    }

    // Creation Menu Flottant
    public void floatingMenu() {

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavorite = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavoriteBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);
        btProfile = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingProfile);
        btSignOut = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingSignOut);
        btBack = findViewById(R.id.btBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMaps = new Intent(ListMuseum.this, MapsActivity.class);
                startActivity(goToMaps);
            }
        });

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btPlaces.setVisibility(View.VISIBLE);
                    btProfile.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btPlaces.setVisibility(View.GONE);
                    btProfile.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFavorites = new Intent(ListMuseum.this, FavoritesActivity.class);
                startActivity(goToFavorites);
            }
        });

        btPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoListMuseum = new Intent(ListMuseum.this, MapsActivity.class);
                startActivity(gotoListMuseum);
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ListMuseum.this, SignIn.class));
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            btBurger.setOnClickListener(new View.OnClickListener() {
                int i = 0;

                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    if (i == 0) {
                        TransitionManager.beginDelayedTransition(transitionContainer);
                        btFavorite.setVisibility(View.VISIBLE);
                        btPlaces.setVisibility(View.VISIBLE);
                        btProfile.setVisibility(View.VISIBLE);
                        btSignOut.setVisibility(View.VISIBLE);
                        i++;
                    } else if (i == 1) {
                        TransitionManager.beginDelayedTransition(transitionContainer);
                        btFavorite.setVisibility(View.GONE);
                        btPlaces.setVisibility(View.GONE);
                        btProfile.setVisibility(View.GONE);
                        btSignOut.setVisibility(View.GONE);
                        i = 0;
                    }
                }
            });
            btProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ListMuseum.this, Profile.class));
                }
            });

        } else {
            // No user is signed in
            btProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ListMuseum.this, SignIn.class));
                }
            });
        }
    }
}
