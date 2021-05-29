package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.npe_05_mymusic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd;
    ImageButton btnBack;
    TextView tvEditProfile;
    private TextView tvName;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Find View
        btnAdd = findViewById(R.id.btn_add);
        btnBack = findViewById(R.id.ib_back);
        tvEditProfile = findViewById(R.id.tv_edit);
        // Onclick Listner
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);

        //edit userName
        tvName = (TextView) findViewById(R.id.tv_name);
        reference = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("fullName").getValue(String.class);
                tvName.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                Intent i = new Intent(ProfileActivity.this, UploadActivity.class);
                startActivity(i);
                break;
            case R.id.tv_edit:
                Intent editProfileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(editProfileIntent);
                break;
            case R.id.ib_back:
                Intent back = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(back);
                break;

        }
    }
}