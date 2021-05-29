package com.example.npe_05_mymusic.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_05_mymusic.Activities.LoginActivity;
import com.example.npe_05_mymusic.Activities.MainActivity;
import com.example.npe_05_mymusic.Activities.MusicPlayerActivity;
import com.example.npe_05_mymusic.Activities.ProfileActivity;
import com.example.npe_05_mymusic.Adapaters.EksplorasiAdapter;
import com.example.npe_05_mymusic.Adapaters.RekomendasiAdapter;
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

public class HomeFragment extends Fragment implements View.OnClickListener, RekomendasiAdapter.OnItemClick, EksplorasiAdapter.OnItemClick {

    // widgets
    private TextView tvName;
    private ImageButton btnLogout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    public HomeFragment() {
        // Required empty public constructor
    }

    // recyclerview attr
    private RecyclerView rvRekomendasi, rvEksplorasi;
    private RekomendasiAdapter rekomendasiAdapter;
    private EksplorasiAdapter eksplorasiAdapter;
    private List<SongsModel> rekomendasiList, eksplorasiList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // auth
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User");

        // initialize widgets
        btnLogout = view.findViewById(R.id.ib_logout);
        tvName = (TextView) view.findViewById(R.id.tv_name);

        // button clicked
        btnLogout.setOnClickListener(this);

        // set user data
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

        // create rekomendasi adapter
        rekomendasiList = new ArrayList<>();
        rekomendasiAdapter = new RekomendasiAdapter(rekomendasiList, this);

        // create eksplorasi adapter
        eksplorasiList = new ArrayList<>();
        eksplorasiAdapter = new EksplorasiAdapter(eksplorasiList, this);

        // set recycler rekomendasi
        rvRekomendasi = view.findViewById(R.id.rv_rekomendasi);
        rvRekomendasi.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRekomendasi.setAdapter(rekomendasiAdapter);

        // set recycler eksplorasi
        rvEksplorasi = view.findViewById(R.id.rv_baru);
        rvEksplorasi.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEksplorasi.setAdapter(eksplorasiAdapter);

        // update both list
        updateEksplorasiList();
        updateRekomendasiList();


        return view;
    }

    private void updateRekomendasiList() {
        DatabaseReference rekomendasiRef = FirebaseDatabase.getInstance().getReference()
                .child("Song_List");
        rekomendasiRef.addValueEventListener(new ValueEventListener() {
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
                        rekomendasiList.add(songsModel);
                    }
                    rekomendasiAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Kamu belum menambahkan lagu.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateEksplorasiList() {
        DatabaseReference mySongRef = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                        eksplorasiList.add(songsModel);
                    }
                    eksplorasiAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Kamu belum menambahkan lagu.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_logout:
                mAuth.signOut();
                Intent a = new Intent(getActivity(), LoginActivity.class);
                startActivity(a);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void itemEksplorasiClicked(int position) {
//        Intent goToMusicPlayer = new Intent(getActivity(), MusicPlayerActivity.class);
//        goToMusicPlayer.putExtra("CLASS_ID", 101);
//        startActivity(goToMusicPlayer);
    }

    @Override
    public void itemRekomendasiClicked(int position) {
        Intent goToMusicPlayer = new Intent(getActivity(), MusicPlayerActivity.class);
        goToMusicPlayer.putExtra("CLASS_ID", 101);
        startActivity(goToMusicPlayer);
    }
}