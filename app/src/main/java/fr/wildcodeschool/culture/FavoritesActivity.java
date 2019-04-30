package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    FloatingActionButton btFavorite, btBurger, btPlaces, btProfile, btEvents, btSignOut;
    Button btBack;
    CoordinatorLayout transitionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        floatingMenu();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("favorites");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Museum> listFavorites = new ArrayList<>();
                for (DataSnapshot museumSnapshot : dataSnapshot.getChildren()) {
                    Museum favorites = museumSnapshot.getValue(Museum.class);
                    listFavorites.add(favorites);
                    ListView listEgg = findViewById(R.id.favorites_listView);
                    listFavoritesAdapter adapter = new listFavoritesAdapter(FavoritesActivity.this, listFavorites);
                    listEgg.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void floatingMenu() {

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavorite = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavoriteBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);
        btProfile = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingProfile);
        btEvents = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListEvents);
        btSignOut = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingSignOut);
        btBack = findViewById(R.id.btBackInFavorite);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMaps = new Intent(FavoritesActivity.this, MapsActivity.class);
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
                Intent goToFavorites = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                startActivity(goToFavorites);
            }
        });
        btEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoListMuseum = new Intent(FavoritesActivity.this, EventsActivity.class);
                startActivity(gotoListMuseum);
            }
        });

        btPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoListMuseum = new Intent(FavoritesActivity.this, ListMuseum.class);
                startActivity(gotoListMuseum);
            }
        });


        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(FavoritesActivity.this, SignIn.class));
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
                        btEvents.setVisibility(View.VISIBLE);
                        btSignOut.setVisibility(View.VISIBLE);
                        i++;
                    } else if (i == 1) {
                        TransitionManager.beginDelayedTransition(transitionContainer);
                        btFavorite.setVisibility(View.GONE);
                        btPlaces.setVisibility(View.GONE);
                        btEvents.setVisibility(View.GONE);
                        btSignOut.setVisibility(View.GONE);
                        i = 0;
                    }
                }
            });

        } else {
            // No user is signed in
            btProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(FavoritesActivity.this, SignIn.class));
                }
            });
        }
    }

}
