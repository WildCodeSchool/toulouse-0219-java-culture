package fr.wildcodeschool.culture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText eTeMail;
    private EditText eTpassword;
    private Button bTsignIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        eTeMail = findViewById(R.id.etEmail);
        eTpassword = findViewById(R.id.etPassword);

        bTsignIn = findViewById(R.id.btSignIn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SignIn.this, MapsActivity.class));
                }
            }
        };

        bTsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        String email = eTeMail.getText().toString();
        String password = eTpassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            Toast.makeText(SignIn.this, getString(R.string.all_fields_are_required), Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignIn.this, getString(R.string.wrong_mail_or_password), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
