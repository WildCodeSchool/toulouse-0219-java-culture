package fr.wildcodeschool.culture;

import android.content.Context;
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

import java.util.ArrayList;

public class Event {

    String adresse;
    String descriptif;
    String horaires;
    String name;
    String tarif;

    public Event(String adresse, String descriptif, String horaires, String name, String tarif) {
        this.adresse = adresse;
        this.descriptif = descriptif;
        this.horaires = horaires;
        this.name = name;
        this.tarif = tarif;
    }

    public static void extractAPI(Context context, Boolean dropoff, int zoom, final Event.EventListener listener) {
        String json = null;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                String adresse = "";
                                if (fields.has("lieu_adresse_2")) {
                                    adresse = (String) fields.get("lieu_adresse_2");
                                }

                                String descriptif = (String) fields.get("descriptif_court");
                                String horaires = (String) fields.get("dates_affichage_horaires");
                                String name = (String) fields.get("nom_de_la_manifestation");
                                String tarif = "";
                                if (fields.has("tarif normal")) {
                                    tarif = (String) fields.get("tarif_normal");
                                }

                                Event event = new Event(adresse, descriptif, horaires, name, tarif);
                                events.add(event);
                            }
                            listener.onResult(events);
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public interface EventListener {
        void onResult(ArrayList<Event> events);
    }
}
