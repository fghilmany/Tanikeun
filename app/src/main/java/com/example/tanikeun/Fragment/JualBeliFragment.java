package com.example.tanikeun.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanikeun.AddBarangActivity;
import com.example.tanikeun.JualBeli;
import com.example.tanikeun.JualBeliAdapter;
import com.example.tanikeun.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class JualBeliFragment extends Fragment implements View.OnClickListener {

    TextView tbAddBarang;
    RecyclerView rvJualBeli;
    ArrayList<JualBeli>list;
    JualBeliAdapter adapter;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";


    public JualBeliFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_jual_beli, container, false);

        getUsernameLocal();

        tbAddBarang = fragmentView.findViewById(R.id.tb_add_barang);
        tbAddBarang.setOnClickListener(this);

        rvJualBeli = fragmentView.findViewById(R.id.rv_jual_beli);
        rvJualBeli.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<JualBeli>();

        reference = FirebaseDatabase.getInstance().getReference().child("jual_beli").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    JualBeli p = dataSnapshot1.getValue(JualBeli.class);
                    list.add(p);
                }
                adapter = new JualBeliAdapter(getActivity(),list);
                rvJualBeli.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return (fragmentView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_add_barang:
                Intent toAddBarang = new Intent(getActivity(), AddBarangActivity.class);
                startActivity(toAddBarang);
        }

    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
