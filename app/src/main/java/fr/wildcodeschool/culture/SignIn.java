package fr.wildcodeschool.culture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {


    private Button mBtAlreadyHaveAccount;
    private ProgressBar mProgressBarSign;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtSignIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();


        mEtEmail = findViewById(R.id.etEmail);
        mEtPassword = findViewById(R.id.etPassword);


        mBtAlreadyHaveAccount = findViewById(R.id.btAlreadyhaveAccount);
        mProgressBarSign = findViewById(R.id.progressBarSign);
        mBtSignIn = findViewById(R.id.btSignIn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mProgressBarSign.setVisibility(View.GONE);
                if (firebaseAuth.getCurrentUser() != null) {

                    startActivity(new Intent(SignIn.this, MapsActivity.class));
                }
            }
        };

        mBtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        mBtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(SignIn.this, RegisterActivity.class);
                startActivity(goToRegister);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();
        mProgressBarSign.setVisibility(View.VISIBLE);

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