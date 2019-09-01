package com.example.tanikeun.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tanikeun.R;
import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private TextView tv_nama, tv_ttl,tv_alamat,tv_lokasi_kebun,tv_lama, tv_noHp;
    private ImageView iv_profil;

    DatabaseReference reference;

    String USERNAME_KEY = "usernema_key";
    String username_key = "";
    String username_key_new = "";



    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_profil, container, false);
        
        getUsernameLocal();
        
        tv_nama = fragmentView.findViewById(R.id.tv_nama);
        tv_alamat = fragmentView.findViewById(R.id.tv_alamat);
        tv_lama = fragmentView.findViewById(R.id.tv_lama_berkebun);
        tv_lokasi_kebun = fragmentView.findViewById(R.id.tv_lokasi_kebun);
        tv_ttl = fragmentView.findViewById(R.id.tv_ttl);
        iv_profil = fragmentView.findViewById(R.id.iv_profil);
        tv_noHp = fragmentView.findViewById(R.id.tv_no_hp);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("data_petani").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //pake for agar tidak null
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Picasso.with(getActivity()).load(ds
                            .child("url_foto").getValue().toString())
                            .into(iv_profil);
                    tv_nama.setText(ds.child("nama").getValue().toString());
                    tv_alamat.setText(ds.child("alamat_rumah").getValue().toString());
                    tv_lokasi_kebun.setText(ds.child("alamat_kebun").getValue().toString());
                    tv_lama.setText(ds.child("lama_tani").getValue().toString());
                    tv_ttl.setText(ds.child("ttl").getValue().toString());
                    tv_noHp.setText(ds.child("no_telepon").getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return (fragmentView);
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
