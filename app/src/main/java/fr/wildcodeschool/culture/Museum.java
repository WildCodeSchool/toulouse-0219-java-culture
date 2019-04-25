package fr.wildcodeschool.culture;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

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
    double latitude;
    double longitude;
    private float distance;


    public Museum(String name, String numero, String horaires, String site, String metro, double longitude, double latitude, float distance) {
        this.name = name;
        this.numero = numero;
        this.horaires = horaires;
        this.site = site;
        this.metro = metro;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public Museum() {
    }

    public static void extractJson(Context context, final Location locationUser, Boolean dropoff, int zoom, final MuseumListener listener) {
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
                Location museumLocation = null;
                JSONObject rooter = (JSONObject) root.get(i);
                JSONObject fields = rooter.getJSONObject("fields");
                String name = (String) fields.get("eq_nom_equipement");
                String numero = "";

                JSONArray geoPoint = (JSONArray) fields.getJSONArray("geo_point");
                for (int j = 0; j < geoPoint.length(); j++) {

                    double latitude = (double) geoPoint.getDouble(0);
                    double longitude = (double) geoPoint.getDouble(1);

                   // LatLng coordMuseum = new LatLng(latitude, longitude);
                    museumLocation.setLatitude(latitude);
                    museumLocation.setLongitude(longitude);
                    float distance = museumLocation.distanceTo(locationUser);

                }
                if(fields.has("eq_telephone")) {
                    numero = (String) fields.get("eq_telephone");
                }

                String metro = "";
                if(fields.has("eq_acces_metro")) {
                    metro = (String) fields.get("eq_acces_metro");
                }

                String horaires ="";
                if(fields.has("eq_horaires")) {
                    horaires = (String) fields.get("eq_horaires");
                }

                String site = "";
                if(fields.has("eq_site_web")) {
                    site = (String) fields.get("eq_site_web");
                }
                Museum museum = new Museum(name,numero,horaires,site,metro,0,0,0);
                museums.add(museum);
            }

        } catch(JSONException e) {
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
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public interface MuseumListener {
        void onResult(ArrayList<Museum> museums);
    }
}

