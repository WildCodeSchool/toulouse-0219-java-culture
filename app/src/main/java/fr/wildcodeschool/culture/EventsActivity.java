package fr.wildcodeschool.culture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

import static fr.wildcodeschool.culture.Event.extractAPI;
import static fr.wildcodeschool.culture.Museum.extractJson;

public class EventsActivity extends AppCompatActivity {
    private static boolean dropOff =true;
    private static int zoom = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        extractAPI(EventsActivity.this,dropOff,zoom, new Event.EventListener() {
            @Override
            public void onResult(ArrayList<Event> events) {
                ListView listMenu = findViewById(R.id.lvEvents);
                ListEventsAdapter adapter = new ListEventsAdapter(EventsActivity.this, events);
                listMenu.setAdapter(adapter);
            }
        });

    }
}
