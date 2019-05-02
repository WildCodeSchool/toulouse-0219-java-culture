package fr.wildcodeschool.culture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.transitionseverywhere.TransitionManager;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Community extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1234;
    private static final int REQUEST_GET_SINGLE_FILE = 4321;
    private static final String TYPE_IMAGE = "image/*";
    private static final Glide GlideApp = null;
    FloatingActionButton btFavorite, btBurger, btPlaces, btProfile, btEvents, btSignOut, btCommunity;
    CoordinatorLayout transitionContainer;
    Button btBack;
    private String mCurrentPhotoPath = null;
    private Uri mDownloadUri = null;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        floatingMenu();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Button btGallery = findViewById(R.id.btGallery);
        btGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(TYPE_IMAGE);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE);
            }
        });

        Button btTakePic = findViewById(R.id.btTakePicture);
        btTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        // ouvrir l'application de prise de photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // lors de la validation de la photo
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // créer le fichier contenant la photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                //  gérer l'erreur
            }

            if (photoFile != null) {
                // récupèrer le chemin de la photo
                mDownloadUri = FileProvider.getUriForFile(this,
                        "fr.wildcodeschool.culture.fileprovider",
                        photoFile);
                // déclenche l'appel de onActivityResult
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mDownloadUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GET_SINGLE_FILE) {
                mDownloadUri = data.getData();
            }
            if (requestCode == REQUEST_TAKE_PHOTO) {
            }
            if (mDownloadUri != null) {

                ImageView ivRecupPic = findViewById(R.id.ivRecupPic);
                Glide.with(Community.this).load(mDownloadUri).into(ivRecupPic);

                //firebase
                final StorageReference riversRef = mStorageRef.child("images/" + mDownloadUri.getPath());

                riversRef.putFile(mDownloadUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        Toast.makeText(Community.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                                        ImageView iv = findViewById(R.id.ivFireBase);
                                        Glide.with(Community.this).load(downloadUrl).into(iv);
                                        //Do what you want with the url
                                    }

                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
            }
        }
    }

    // Creation Menu Flottant
    public void floatingMenu() {

        transitionContainer = (CoordinatorLayout) findViewById(R.id.menuLayout);
        btBurger = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingActionButton);
        btFavorite = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingFavoriteBt);
        btPlaces = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListPlaces);
        btProfile = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingProfile);
        btSignOut = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingSignOut);
        btCommunity = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingCommunity);
        btEvents = (FloatingActionButton) transitionContainer.findViewById(R.id.floatingListEvents);
        btBack = findViewById(R.id.btBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMaps = new Intent(Community.this, MapsActivity.class);
                startActivity(goToMaps);
            }
        });

        btBurger.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btPlaces.setVisibility(View.VISIBLE);
                    btEvents.setVisibility(View.VISIBLE);
                    btProfile.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {

                    TransitionManager.beginDelayedTransition(transitionContainer);
                    btPlaces.setVisibility(View.GONE);
                    btEvents.setVisibility(View.GONE);
                    btProfile.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFavorites = new Intent(Community.this, FavoritesActivity.class);
                startActivity(goToFavorites);
            }
        });

        btPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoListMuseum = new Intent(Community.this, ListMuseum.class);
                startActivity(gotoListMuseum);
            }
        });
        btEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoListMuseum = new Intent(Community.this, EventsActivity.class);
                startActivity(gotoListMuseum);
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Community.this, SignIn.class));
            }
        });
        btCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoCommunity = new Intent(Community.this, Community.class);
                startActivity(gotoCommunity);

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            btBurger.setOnClickListener(new View.OnClickListener() {
                int i = 0;

                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    if (i == 0) {
                        TransitionManager.beginDelayedTransition(transitionContainer);
                        btFavorite.setVisibility(View.VISIBLE);
                        btPlaces.setVisibility(View.VISIBLE);
                        btEvents.setVisibility(View.VISIBLE);
                        btSignOut.setVisibility(View.VISIBLE);
                        btCommunity.setVisibility(View.VISIBLE);
                        i++;
                    } else if (i == 1) {
                        TransitionManager.beginDelayedTransition(transitionContainer);
                        btFavorite.setVisibility(View.GONE);
                        btPlaces.setVisibility(View.GONE);
                        btEvents.setVisibility(View.GONE);
                        btSignOut.setVisibility(View.GONE);
                        btCommunity.setVisibility(View.GONE);
                        i = 0;
                    }
                }
            });
        } else {
            // No user is signed in
            btProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Community.this, SignIn.class));
                }
            });
        }
    }
}