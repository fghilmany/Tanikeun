package com.example.tanikeun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailJualBeliAct extends AppCompatActivity implements View.OnClickListener {

    TextView tvNama, tvBerat, tvHarga, tvKualitas;
    ImageView ivFoto;
    Button btnAddBarang, btnHapus;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jual_beli);

        getUsernameLocal();

        ivFoto = findViewById(R.id.iv_foto);
        tvNama = findViewById(R.id.tv_nama_panen);
        tvBerat = findViewById(R.id.tv_berat);
        tvHarga = findViewById(R.id.tv_harga);
        tvKualitas = findViewById(R.id.tv_kualitas);
        btnAddBarang = findViewById(R.id.btn_edit);
        btnAddBarang.setOnClickListener(this);
        btnHapus = findViewById(R.id.btn_hapus);
        btnHapus.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        final String id_barang_new = bundle.getString("id_barang");
        reference = FirebaseDatabase.getInstance().getReference().child("jual_beli").child(username_key_new).child(id_barang_new);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //cek apakah value child null (jika tidak ada,ketika hapus data dibawah akan null)
                        if (dataSnapshot.getValue()!=null){
                            tvNama.setText(dataSnapshot.child("nama_panen").getValue().toString());
                            tvBerat.setText(dataSnapshot.child("berat").getValue().toString());
                            tvHarga.setText(dataSnapshot.child("harga").getValue().toString());
                            tvKualitas.setText(dataSnapshot.child("kualitas").getValue().toString());

                            Picasso.with(DetailJualBeliAct.this).load(dataSnapshot.child("url_foto_panen").getValue().toString()).into(ivFoto);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hapus:
                Bundle bundle = getIntent().getExtras();
                final String id_barang_new = bundle.getString("id_barang");

                reference =FirebaseDatabase.getInstance().getReference().child("jual_beli").child(username_key_new)
                        ;
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.child(id_barang_new).getRef().removeValue();


                        Intent goHome = new Intent(DetailJualBeliAct.this, MainActivity.class);
                        startActivity(goHome);
                        Toast.makeText(getApplicationContext(),"Berhasil Menghapus Barang",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });

                break;
            case R.id.btn_edit:
                reference = FirebaseDatabase.getInstance().getReference();


                break;
        }

    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
