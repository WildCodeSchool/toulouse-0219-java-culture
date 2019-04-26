package fr.wildcodeschool.culture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("favorites");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Museum> listFavorites = new ArrayList<>();
                for (DataSnapshot museumSnapshot : dataSnapshot.getChildren()) {
                    Museum favorites = museumSnapshot.getValue(Museum.class);
                    listFavorites.add(favorites);

                    ListView listEgg = findViewById(R.id.favorites_listView);
                    listFavoritesAdapter adapter = new listFavoritesAdapter(FavoritesActivity.this, listFavorites);
                    listEgg.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
