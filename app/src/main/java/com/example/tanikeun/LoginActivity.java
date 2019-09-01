package com.example.tanikeun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    ProgressBar progressBar;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPassword = findViewById(R.id.edt_password);
        edtUsername = findViewById(R.id.edt_username);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnLogin.setText(null);
                String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnLogin.setEnabled(true);
                    btnLogin.setText("LOGIN");
                }else {
                    reference = FirebaseDatabase.getInstance().getReference().child("data_petani").child(username);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                //ambil data password dari firebase
                                String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                //validasi password
                                if (password.equals(passwordFromFirebase)){
                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key, edtUsername.getText().toString());
                                    editor.apply();

                                    //pindah activity
                                    Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(goToMain);
                                    finish();

                                    progressBar.setVisibility(View.GONE);
                                }else {
                                    Toast.makeText(getApplicationContext(),"Password Salah", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    btnLogin.setEnabled(true);
                                    btnLogin.setText("LOGIN");
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"Username Salah", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                btnLogin.setEnabled(true);
                                btnLogin.setText("LOGIN");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
        }

    }
}
