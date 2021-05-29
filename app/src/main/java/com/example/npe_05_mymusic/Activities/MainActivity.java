package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.npe_05_mymusic.Fragments.FavoriteFragment;
import com.example.npe_05_mymusic.Fragments.HomeFragment;
import com.example.npe_05_mymusic.Fragments.SearchFragment;
import com.example.npe_05_mymusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private Map<Integer, Fragment> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bn_main);
        fragmentMap = new HashMap<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

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

        mAuth.addAuthStateListener(mAuthStateListener);
        fragmentMap.put(R.id.menu_item_home, HomeFragment.newInstance());
        fragmentMap.put(R.id.menu_item_search, SearchFragment.newInstance());
        fragmentMap.put(R.id.menu_item_favorite, FavoriteFragment.newInstance());
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_profile){
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        Fragment fragment = fragmentMap.get(item.getItemId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, fragment)
                .commit();
        return true;
    }
}