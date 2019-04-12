package fr.wildcodeschool.culture;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Museum {

    private static ArrayList<Museum> museums = new ArrayList<>();
    String name;
    String numero;
    String horaires;
    String site;
    String metro;


    public Museum(String name, String numero, String horaires, String site, String metro) {
        this.name = name;
        this.numero = numero;
        this.horaires = horaires;
        this.site = site;
        this.metro = metro;
    }

    public static void extractJson(Context context, Boolean dropoff, int zoom, final MuseumListener listener) {

        String json = null;

        try {
            InputStream is = context.getAssets().open("Toulouse-musees.json");
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
                String name = (String) fields.get("eq_nom_equipement");
                String numero = "";
                if (fields.has("eq_telephone")) {
                    numero = (String) fields.get("eq_telephone");

                }
                String metro = "";
                if (fields.has("eq_acces_metro")) {
                    metro = (String) fields.get("eq_acces_metro");

                }
                String horaires ="";
                if (fields.has("eq_horaires")){
                    horaires = (String) fields.get("eq_horaires");
                }
                String site = "";
                if(fields.has("eq_site_web")){
                    site = (String) fields.get("eq_site_web");
                }

                Museum museum = new Museum(name, numero, horaires, site, metro);
                museums.add(museum);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onResult(museums);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }


    public interface MuseumListener {
        void onResult(ArrayList<Museum> museums);
    }

}

