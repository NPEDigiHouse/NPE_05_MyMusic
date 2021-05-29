package com.example.npe_05_mymusic.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.npe_05_mymusic.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                Intent i = new Intent(ProfileActivity.this, UploadActivity.class);
                startActivity(i);
                break;
        }
    }
}