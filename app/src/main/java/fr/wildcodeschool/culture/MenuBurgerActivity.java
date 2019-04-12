package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.RestrictTo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.transitionseverywhere.TransitionManager;

public class MenuBurgerActivity extends AppCompatActivity {
        FloatingActionButton btFavorite, btBurger, btPlaces;
        CoordinatorLayout transitionContainer;
        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_burger);
            floatingMenu();
        }

    // Creation Menu
    public void floatingMenu() {

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavorite = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavoriteBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorite.setVisibility(View.VISIBLE);
                    btPlaces.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorite.setVisibility(View.GONE);
                    btPlaces.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });
    }
}
