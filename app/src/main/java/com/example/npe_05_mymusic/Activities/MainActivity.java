package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.npe_05_mymusic.Fragments.FavoriteFragment;
import com.example.npe_05_mymusic.Fragments.HomeFragment;
import com.example.npe_05_mymusic.Fragments.ProfileFragment;
import com.example.npe_05_mymusic.Fragments.SearchFragment;
import com.example.npe_05_mymusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_main, new HomeFragment()).commit();
        }
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null){
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.menu_item_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.menu_item_favorite:
                            selectedFragment = new FavoriteFragment();
                            break;
                        case R.id.menu_item_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.menu_item_home:
                            selectedFragment = new HomeFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,
                            selectedFragment).commit();
                    return true;
                }
            };
}