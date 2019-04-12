package fr.wildcodeschool.culture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Events extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=agenda-des-manifestations-culturelles-so-toulouse&facet=type_de_manifestation&refine.type_de_manifestation=Culturelle";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray records = response.getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject rec = (JSONObject) records.get(i);
                                JSONObject fields = rec.getJSONObject("fields");
                                for (int b = 0; b < fields.length(); b++){
                                    String adresse = (String) fields.get("lieu_adresse_2");
                                    String descriptif = (String) fields.get("descriptif_court");
                                    String horaires = (String) fields.get("dates_affichage_horaires");
                                    String name = (String) fields.get("nom_de_la_manifestation");
                                    String tarif =  (String) fields.get("tarif_normal");
                                    JSONArray geolocalisation =(JSONArray) fields.get("geo_point");
                                    Double latitude = (Double) geolocalisation.get(0);
                                    Double longitude = (Double) geolocalisation.get(1);
                                }
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
        // On ajoute la requête à la file d'attente
        requestQueue.add(jsonObjectRequest);
    }
}
