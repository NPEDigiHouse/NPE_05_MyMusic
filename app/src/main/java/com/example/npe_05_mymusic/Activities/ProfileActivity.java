package com.example.npe_05_mymusic.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.npe_05_mymusic.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd;
    ImageButton btnBack;
    TextView tvEditProfile;
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