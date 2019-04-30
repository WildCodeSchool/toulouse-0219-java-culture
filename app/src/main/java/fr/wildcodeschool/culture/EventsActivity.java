package fr.wildcodeschool.culture;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import static fr.wildcodeschool.culture.Event.extractAPI;

public class EventsActivity extends AppCompatActivity {
    private static boolean dropOff = true;
    private static int zoom = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Singleton singleton = Singleton.getInstance();
        Location coord = singleton.getLocationUser();

        extractAPI(EventsActivity.this,coord, dropOff, zoom, new Event.EventListener() {
            @Override
            public void onResult(ArrayList<Event> events) {
                ListView listMenu = findViewById(R.id.lvEvents);
                ListEventsAdapter adapter = new ListEventsAdapter(EventsActivity.this, events);
                listMenu.setAdapter(adapter);
            }
        });

    }
}
