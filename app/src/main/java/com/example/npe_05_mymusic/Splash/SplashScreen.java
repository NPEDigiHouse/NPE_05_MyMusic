package com.example.npe_05_mymusic.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.npe_05_mymusic.Activities.LoginActivity;
import com.example.npe_05_mymusic.Activities.MainActivity;
import com.example.npe_05_mymusic.Activities.WelcomeActivity;
import com.example.npe_05_mymusic.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(SplashScreen.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }},2000);
    }
}
