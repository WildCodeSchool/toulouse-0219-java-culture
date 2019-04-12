package fr.wildcodeschool.culture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

import static fr.wildcodeschool.culture.Museum.extractJson;

public class ListMuseum extends AppCompatActivity {
    private static boolean dropOff = true;
    private static int zoom = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_museum);

        extractJson(ListMuseum.this,dropOff,zoom, new Museum.MuseumListener() {
            @Override
            public void onResult(ArrayList<Museum> museums) {
                ListView listMenu = findViewById(R.id.listView);
                ListMuseumAdapter adapter = new ListMuseumAdapter(ListMuseum.this, museums);
                listMenu.setAdapter(adapter);
            }
        });
    }

}
