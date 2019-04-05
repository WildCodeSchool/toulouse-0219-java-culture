package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.transitionseverywhere.TransitionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ListMuseum extends AppCompatActivity {
    FloatingActionButton btFavorit, btBurger, btPlaces;
    CoordinatorLayout transitionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_museum);
        FloatingMenu();

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
                for (int b = 0; b < fields.length(); b++){
                    String name = (String) fields.get("eq_nom_equipement");
                    String numero = (String) fields.get("eq_telephone");
                    String horaires = (String) fields.get("eq_horaires");
                    String site = (String) fields.get("eq_site_web");
                    String metro =  (String) fields.get("eq_acces_metro");

                    Museum musées = new Museum(name,numero,horaires,site,metro);

                    List<Museum> menu = Arrays.asList(musées);

                    ListView listMenu = findViewById(R.id.listView);
                    ListMuseumAdapter adapter = new ListMuseumAdapter(ListMuseum.this, menu);
                    listMenu.setAdapter(adapter);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Creation Menu Flottant
    public void FloatingMenu(){

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavorit = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavoritBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorit.setVisibility(View.VISIBLE);
                    btPlaces.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btFavorit.setVisibility(View.GONE);
                    btPlaces.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });
    }
}
