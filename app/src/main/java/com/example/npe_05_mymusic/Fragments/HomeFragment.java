package com.example.npe_05_mymusic.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.npe_05_mymusic.Activities.LoginActivity;
import com.example.npe_05_mymusic.Activities.MainActivity;
import com.example.npe_05_mymusic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private TextView tvName;
    private ImageButton btnLogout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnLogout = view.findViewById(R.id.ib_logout);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        reference = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();
        btnLogout.setOnClickListener(this);

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
        return view;
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
}