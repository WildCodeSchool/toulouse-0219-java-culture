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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegLastname, mRegFirstname, mRegEmail, mRegPassword, mRegConfirmPassword;
    private Button mRegBtRegister;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBarReg;
    private FirebaseDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegLastname = findViewById(R.id.etLastName);
        mRegFirstname = findViewById(R.id.etFirstName);
        mRegEmail = findViewById(R.id.etEmailRegister);
        mRegPassword = findViewById(R.id.etPasswordRegister);
        mRegConfirmPassword = findViewById(R.id.etConfirmPassword);
        mRegBtRegister = findViewById(R.id.btRegister);
        mAuth = FirebaseAuth.getInstance();
        mProgressBarReg = findViewById(R.id.progressBar);
        Button btAlreadyHaveAccount = findViewById(R.id.btHaveAccount);

        btAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignIn = new Intent(RegisterActivity.this, SignIn.class);
                startActivity(goToSignIn);
            }
        });

        mRegBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String lastName = mRegLastname.getText().toString();
                final String firstName = mRegFirstname.getText().toString();
                String emailReg = mRegEmail.getText().toString();
                String passwordReg = mRegPassword.getText().toString();
                String passwordConfirmReg = mRegConfirmPassword.getText().toString();

                if (!TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(emailReg) && !TextUtils.isEmpty(passwordReg) && !TextUtils.isEmpty(passwordConfirmReg)) {
                    if (passwordReg.equals(passwordConfirmReg)) {

                        mProgressBarReg.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailReg, passwordReg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressBarReg.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference nameUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                    Map newPost = new HashMap();
                                    newPost.put("lastname", lastName);
                                    newPost.put("firstname", firstName);
                                    nameUser.setValue(newPost);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = user.getUid();

                                    Toast.makeText(RegisterActivity.this, getString(R.string.welcome) + " "+ lastName + " " + firstName , Toast.LENGTH_LONG).show();
                                    goToMapsActivity();

                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, getString(R.string.error) + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.confirmation_password_does_not_match), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.all_fields_are_required), Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToMapsActivity();
        }
    }

    private void goToMapsActivity() {
        Intent signInPageIntent = new Intent(RegisterActivity.this, MapsActivity.class);
        startActivity(signInPageIntent);
        finish();
    }
}
