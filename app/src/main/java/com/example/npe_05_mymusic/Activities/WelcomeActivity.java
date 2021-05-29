package com.example.npe_05_mymusic.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.npe_05_mymusic.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnDaftar, btnNoHp, btnGoogle;
    private TextView tvMasuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnDaftar = (Button) findViewById(R.id.btn_daftar);
        btnNoHp = (Button) findViewById(R.id.btn_nohp);
        btnGoogle = (Button) findViewById(R.id.btn_noemail);
        tvMasuk = (TextView) findViewById(R.id.tv_masuk);

        btnNoHp.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        tvMasuk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_masuk:
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                break;
            case R.id.btn_noemail:

                break;
            case R.id.btn_nohp:

                break;
            case R.id.btn_daftar:
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                break;
        }
    }
}