package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.npe_05_mymusic.Adapaters.SongsAdapter;
import com.example.npe_05_mymusic.Models.songs.SongsModel;
import com.example.npe_05_mymusic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, SongsAdapter.OnItemClick {

    // auth
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // recycler attr
    private RecyclerView rvMySongs;
    private SongsAdapter songsAdapter;
    private List<SongsModel> songList;

    // widgets
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get user
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // initialize adapter with empty list
        songList = new ArrayList<>();
        songsAdapter = new SongsAdapter(this, songList);

        // set recyclerview
        rvMySongs = findViewById(R.id.rv_my_songs);
        rvMySongs.setLayoutManager(new LinearLayoutManager(this));
        rvMySongs.setAdapter(songsAdapter);

        // update song list
        updateSongList();

        // initialize widgets
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

    private void updateSongList() {
        DatabaseReference mySongRef = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(mUser.getUid())
                .child("song_list");
        mySongRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    // update list
                    SongsModel songsModel = new SongsModel(
                            data.child("title").getValue().toString(),
                            data.child("artist").getValue().toString(),
                            data.child("genre").getValue().toString(),
                            data.child("song_url").getValue().toString(),
                            data.child("cover_url").getValue().toString()
                    );
                    songList.add(songsModel);
                }
                songsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void btnMoreClicked(int position) {
        Toast.makeText(this, songList.get(position).getTitle() + " btn more clicked", Toast.LENGTH_SHORT).show();
    }
}