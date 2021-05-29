package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_05_mymusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button btnUpload, btnDeploy;
    private ConstraintLayout btnUploadImg, btnUploadSong;
    private EditText etTitle, etArtist, etGenre;
    private ImageView ivCover, btnPlaySong;
    private AutoCompleteTextView tvGenre;
    private Spinner spinGenre;
    private DatabaseReference dbReference;
    private String name, artist, song_url, cover_url;
    private Map<String, Object> map = new HashMap<>();
    private TextView tvURLCover, tvURLAudio;

    private final int PICK_IMAGE_REQUEST = 22;
    private final int PICK_AUDIO_REQUEST = 33;
    private String[] genre = {"Pop", "RnB", "Dance"};

    // Uri indicates, where the image will be picked from
    private Uri audioFilePath, imageFilePath;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        btnUpload = findViewById(R.id.btn_upload);
        btnUploadImg = findViewById(R.id.tombol_gambar);
        btnUploadSong = findViewById(R.id.cv_lagu);
        btnPlaySong = findViewById(R.id.icon_song);
        etTitle = findViewById(R.id.et_song_title);
        etArtist = findViewById(R.id.et_song_artist);
        tvGenre = findViewById(R.id.tv_genre);
        tvURLAudio = findViewById(R.id.tvURLAudio);
        tvURLCover = findViewById(R.id.tvURLCover);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        dbReference = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genre);
        tvGenre.setAdapter(adapter);
        tvGenre.setOnItemSelectedListener(this);

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnUploadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAudio();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();

            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void selectAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Audio from here..."),
                PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            imageFilePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), imageFilePath);
                ivCover.setImageBitmap(bitmap);
                Log.d("PATH", String.valueOf(imageFilePath));
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            audioFilePath = data.getData();

            Uri uriData = Uri.parse(String.valueOf(audioFilePath));
            Log.d("PATH", String.valueOf(uriData));
        }
    }

    private void uploadData() {
        if (imageFilePath != null && audioFilePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            name = etTitle.getText().toString();
            artist = etArtist.getText().toString();

            StorageReference ref = storageReference.child("songs/").child(name + " - " + artist +"/");

            ref.child("cover/").putFile(imageFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            cover_url = uri.toString();

                            tvURLCover.setText(cover_url);
                            map.put("cover_url", uri.toString());
                            Toast.makeText(UploadActivity.this, cover_url, Toast.LENGTH_SHORT).show();
                            Log.d("URL", cover_url);
                        }
                    });

                    Toast.makeText(UploadActivity.this,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });

            ref.child("audio/").putFile(audioFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            song_url = uri.toString();

                            tvURLAudio.setText(song_url);
                            map.put("song_url", uri.toString());
                            Log.d("URL", song_url);
                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(UploadActivity.this, "Audio Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }


        deployToDB();
    }

    private void deployToDB() {
        name = etTitle.getText().toString();
        artist = etArtist.getText().toString();


        map.put("title", etTitle.getText().toString());
        map.put("artist", etArtist.getText().toString());
        map.put("genre", tvGenre.getText().toString());
        map.put("song_url", tvURLAudio.getText().toString());
        map.put("cover_url", tvURLCover.getText().toString());

        dbReference.child("User").child(mUser.getUid()).child("song_list").child(name + " - " + artist).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UploadActivity.this, "Berhasil deploy ke DB", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}