package fr.wildcodeschool.culture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView fav = findViewById(R.id.textView2);
                for (DataSnapshot museumSnapshot : dataSnapshot.getChildren()) {
                    Museum favorites = museumSnapshot.getValue(Museum.class);
                    Toast.makeText(FavoritesActivity.this, favorites.getName(),
                            Toast.LENGTH_LONG).show();
                    fav.append(favorites.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

}
