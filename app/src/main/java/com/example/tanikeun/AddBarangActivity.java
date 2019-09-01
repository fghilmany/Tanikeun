package com.example.tanikeun;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class AddBarangActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtNama, edtBerat, edtHarga, edtKualitas;
    ImageView btnAddFoto, ivFoto;
    Button btnAddBarang;
    ProgressBar progressBar;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    DatabaseReference reference1;
    StorageReference storage;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    BarangJual barang;
    long maxId=0;
    Integer jumlahHarga = 0;
    Integer jumlahBerat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang);

        getUsernameLocal();

        edtBerat = findViewById(R.id.edt_qty);
        progressBar = (ProgressBar)findViewById(R.id.progress_barr);
        edtHarga = findViewById(R.id.edt_harga);
        edtKualitas = findViewById(R.id.kualitas);
        edtNama = findViewById(R.id.edt_nama_sayur);
        ivFoto = findViewById(R.id.iv_add_jual);
        btnAddFoto = findViewById(R.id.btn_add_foto);
        btnAddFoto.setOnClickListener(this);
        btnAddBarang = findViewById(R.id.add_barang);
        btnAddBarang.setOnClickListener(this);
        barang = new BarangJual();
        reference = FirebaseDatabase.getInstance().getReference().child("jual_beli").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxId=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_foto:
                findFoto();
                break;
            case R.id.add_barang:
                progressBar.setVisibility(View.VISIBLE);
                btnAddBarang.setText(null);
                btnAddBarang.setEnabled(false);

                final String id_barang = Long.toString(maxId+1);

                reference1 = FirebaseDatabase.getInstance().getReference()
                        .child("jual_beli")
                        .child(username_key_new)
                        .child(username_key_new+id_barang);
                storage = FirebaseStorage.getInstance().getReference().child("Fotouser").child(username_key_new);

                if (photo_location != null){
                    final StorageReference storageReference =
                            storage.child(System.currentTimeMillis()+"."+getFileExtension(photo_location));
                    storageReference.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference1.getRef().child("id_barang").setValue(username_key_new+id_barang).toString();
                                            reference1.getRef().child("url_foto_panen").setValue(uri_photo);
                                            reference1.getRef().child("nama_panen").setValue(edtNama.getText().toString());
                                            reference1.getRef().child("harga").setValue(Integer.parseInt(String.valueOf(edtHarga.getText())));
                                            reference1.getRef().child("kualitas").setValue(edtKualitas.getText().toString());
                                            reference1.getRef().child("berat").setValue(Integer.parseInt(String.valueOf(edtBerat.getText())));
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Intent goHome = new Intent(AddBarangActivity.this, MainActivity.class);
                                            startActivity(goHome);
                                            Toast.makeText(getApplicationContext(),"Berhasil Menambah Barang",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            btnAddBarang.setText("TAMBAH BARANG");
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                }


                break;
        }

    }

    String getFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findFoto(){
        Intent pic = new Intent();
        pic.setType("image/+");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerInside().fit().into(ivFoto);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
