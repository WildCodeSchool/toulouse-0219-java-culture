package fr.wildcodeschool.culture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText regLastname, regFirstname, regEmail, regPassword, regConfirmPassword;
    private Button regBtRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regLastname = findViewById(R.id.etLastName);
        regFirstname = findViewById(R.id.etFirstName);
        regEmail = findViewById(R.id.etEmail);
        regPassword = findViewById(R.id.etPasswordRegister);
        regConfirmPassword = findViewById(R.id.etConfirmPassword);
        regBtRegister = findViewById(R.id.btRegister);

    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
        }
    }
}
