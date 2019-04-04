package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.support.annotation.RestrictTo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.transitionseverywhere.TransitionManager;

public class MenuBurgerActivity extends AppCompatActivity {


        FloatingActionButton btFavoris, btBurger, btPlaces;
        CoordinatorLayout transitionContainer;
        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_burger);
            menuBurger();
        }
        public void menuBurger(){

            transitionContainer = (CoordinatorLayout) findViewById(R.id.myLayout);
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

            transitionContainer.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    btFavoris.setVisibility(View.GONE);
                    btPlaces.setVisibility(View.GONE);

                }
            });
        }
}
