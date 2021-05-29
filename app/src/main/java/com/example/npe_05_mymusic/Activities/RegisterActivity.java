package com.example.npe_05_mymusic.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.npe_05_mymusic.Models.User;
import com.example.npe_05_mymusic.R;
import androidx.annotation.NonNull;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSignUp;
    private TextView toLogin;
    private FirebaseAuth mAuth;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        ibBack = findViewById(R.id.ib_back);
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnSignUp = (Button) findViewById(R.id.btn_masuk);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String fullName = etName.getText().toString();
                if (fullName.isEmpty()){
                    etName.setError("name is required");
                    etName.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    etEmail.setError("email is required");
                    etEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    etPassword.setError("password is required");
                    etPassword.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("please provide valid email");
                    etEmail.requestFocus();
                    return;
                }
                if (password.length() < 6){
                    etPassword.setError("password should be 6 character");
                    etPassword.requestFocus();
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User users = new User(fullName, email);
                            FirebaseDatabase.getInstance().getReference("User").
                                    child(FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getUid())
                                    .setValue(users)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "success register", Toast.LENGTH_LONG).show();
                                                etEmail.setText("");
                                                etName.setText("");
                                                etPassword.setTex

                                                // go to main
                                                Intent goToMain = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(goToMain);
                                                finishAffinity();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "failed to add data in firebase", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "failed registration",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}