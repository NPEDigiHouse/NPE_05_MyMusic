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
import android.widget.ImageButton;
import android.widget.TextView;
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
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // recycler attr
    private RecyclerView rvMySongs;
    private SongsAdapter songsAdapter;
    private List<SongsModel> songList;

    // widgets
    private Button btnAdd;
    private ImageButton btnBack;
    private TextView tvEditProfile, tvName;

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

    private void updateSongList() {
        DatabaseReference mySongRef = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(mUser.getUid())
                .child("song_list");
        mySongRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String defaultSong = "https://firebasestorage.googleapis.com/v0/b/my-music-e4bf3.appspot.com/o/songs%2FBroken%20Arrow%20-%20Avicii%2Faudio?alt=media&token=7e758d81-669d-4dfc-a36e-6f23c5d31a30";
                    String defaultCover = "https://firebasestorage.googleapis.com/v0/b/my-music-e4bf3.appspot.com/o/songs%2FBroken%20Arrow%20-%20Avicii%2Fcover?alt=media&token=9c6f4963-8cf8-4f69-b9f5-0602c66215b8";
                    for (DataSnapshot data : snapshot.getChildren()) {
                        // update list
                        SongsModel songsModel = new SongsModel(
                                data.child("title").getValue().toString(),
                                data.child("artist").getValue().toString(),
                                data.child("genre").getValue().toString(),
                                data.child("song_url").exists() ? data.child("song_url").getValue().toString() : defaultSong,
                                data.child("cover_url").exists() ? data.child("cover_url").getValue().toString() : defaultCover
                        );
                        songList.add(songsModel);
                    }
                    songsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProfileActivity.this, "Kamu belum menambahkan lagu.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void btnDeleteCLicked(int position) {
        // get tim reference
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("song_list");
        teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pos = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (pos == position) {
                        // delete value
                        teamsRef.child(data.getKey()).removeValue();
                    }
                    pos++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "Tim berhasil dihapus", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
        finish();
    }
}