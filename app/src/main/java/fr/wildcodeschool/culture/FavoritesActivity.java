package fr.wildcodeschool.culture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final List<Museum> listFavorites = new ArrayList<>();
                for (final DataSnapshot museumSnapshot : dataSnapshot.getChildren()) {
                    final Museum favorites = museumSnapshot.getValue(Museum.class);
                    listFavorites.add(favorites);

                    final ListView listFavorite = findViewById(R.id.favorites_listView);
                    final listFavoritesAdapter adapter = new listFavoritesAdapter(FavoritesActivity.this, listFavorites);
                    listFavorite.setAdapter(adapter);
                    listFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            listFavorites.remove(position);
                            adapter.notifyDataSetChanged();
                            deleteMuseum(favorites);
                        }

                        private void deleteMuseum(String favorites) {
                            DatabaseReference dbMuseum = FirebaseDatabase.getInstance().getReference("favorites").child(favorites);
                            dbMuseum.removeValue();
                            Toast.makeText(FavoritesActivity.this, "Museum is Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
