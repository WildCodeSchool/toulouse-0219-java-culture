package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import static fr.wildcodeschool.culture.Museum.extractJson;

public class ListMuseum extends AppCompatActivity {
    private static boolean dropOff = true;
    private static int zoom = 15;
    FloatingActionButton btFavorite, btBurger, btPlaces, btSignOut;
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
        btSignOut = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingSignOut);

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if(i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorite.setVisibility(View.VISIBLE);
                    btPlaces.setVisibility(View.VISIBLE);
                    btSignOut.setVisibility(View.VISIBLE);
                    i++;
                } else if(i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorite.setVisibility(View.GONE);
                    btPlaces.setVisibility(View.GONE);
                    btSignOut.setVisibility(View.GONE);
                    i = 0;
                }
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
    }
}
