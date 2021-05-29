package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.npe_05_mymusic.Models.User;
import com.example.npe_05_mymusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    //Instance Firebase data
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    //instance widget
    private ImageView ivContainerImage;
    private TextInputEditText etName, etEmail, etPassword;
    private Button btnSimpan;
    private ImageButton btnBack;

    // attributes
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        widgetInit();
        setDataUser();

        btnBack.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);
        ivContainerImage.setOnClickListener(this);
    }

    private void setDataUser() {

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                etName.setText(user.getFullName());
                etEmail.setText(user.getEmail());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Fail to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void widgetInit() {
        ivContainerImage = findViewById(R.id.iv_change_image);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnBack = findViewById(R.id.ib_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_change_image:
                openImageIntent();
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_simpan:
                btnSimpan.setEnabled(false);
                btnSimpan.setText("Loading...");
                saveThisChange();
                break;
        }
    }

    private void saveThisChange() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String fullName = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (fullName.isEmpty()){
            etName.setError("name is required");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            etEmail.setError("email is required");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etPassword.setError("password is required");
            etPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("please provide valid email");
            etEmail.requestFocus();
            return;
        }
        if (password.length() < 6){
            etPassword.setError("password should be 6 character");
            etPassword.requestFocus();
            return;
        }

        Map<String, Object> newDataMap = new HashMap<>();
        newDataMap.put("fullName", fullName);
        newDataMap.put("email", email);

        reference.updateChildren(newDataMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(EditProfileActivity.this, "Successfully edit profile", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Failed, try login again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openImageIntent() {
        Intent goToFindImage = new Intent();
        goToFindImage.setType("image/*");
        goToFindImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(goToFindImage, 1);
    }
}