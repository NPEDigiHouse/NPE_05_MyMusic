package com.example.npe_05_mymusic.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.npe_05_mymusic.Models.songs.SongsModel;
import com.example.npe_05_mymusic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MusicPlayerActivity extends AppCompatActivity {
    private ImageView ivCover, ivPlay;
    private TextView tvTitle, tvArtist, tvPlay;
    private ImageButton ibBack;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private int classID;
    private String childID, songsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        ivCover = findViewById(R.id.iv_album);
        ivPlay = findViewById(R.id.iv_play);
        tvTitle = findViewById(R.id.tv_title);
        tvArtist = findViewById(R.id.tv_artis);
        tvPlay = findViewById(R.id.tv_play);

        classID = getIntent().getIntExtra("CLASS_ID", 0);
        childID = getIntent().getStringExtra("CHILD_ID");
        switch (classID) {
            case 101:
                reference.child("User").child(mUser.getUid()).child("song_list").child(childID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SongsModel model = snapshot.getValue(SongsModel.class);

                        songsURL = model.getSong_url();

                        Glide.with(MusicPlayerActivity.this).load(model.getCover_url()).into(ivCover);
                        tvTitle.setText(model.getTitle());
                        tvArtist.setText(model.getArtist());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case 102:
                reference.child("Song_List").child(childID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SongsModel model = snapshot.getValue(SongsModel.class);

                        songsURL = model.getSong_url();

                        Glide.with(MusicPlayerActivity.this).load(model.getCover_url()).into(ivCover);
                        tvTitle.setText(model.getTitle());
                        tvArtist.setText(model.getArtist());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songsURL));
                if (tvPlay.getText().toString().equalsIgnoreCase("play")) {
                    mediaPlayer.start();
                    ivPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    tvPlay.setText("pause");
                } else {
                    mediaPlayer.pause();
                    ivPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    tvPlay.setText("play");
                }
            }
        });

    }
}