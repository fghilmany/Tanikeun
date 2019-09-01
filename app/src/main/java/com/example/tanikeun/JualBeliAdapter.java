package com.example.tanikeun;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JualBeliAdapter extends RecyclerView.Adapter<JualBeliAdapter.MyViewHolder> {

    Context context;
    ArrayList<JualBeli> jualBeli;

    public JualBeliAdapter(Context c, ArrayList<JualBeli> p){
        context = c;
        jualBeli = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_jual, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvNamaPanen.setText(jualBeli.get(i).getNama_panen());
        myViewHolder.tvBerat.setText(Integer.toString(jualBeli.get(i).getBerat()));
        myViewHolder.idBarang.setText(jualBeli.get(i).getId_barang());
        Picasso.with(myViewHolder.iv_foto_panen.getContext())
                .load(jualBeli.get(i).getUrl_foto_panen())
                .into(myViewHolder.iv_foto_panen);

        final String getIdBarang = jualBeli.get(i).getId_barang();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goDetail = new Intent(context, DetailJualBeliAct.class);
                goDetail.putExtra("id_barang",getIdBarang);
                context.startActivity(goDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jualBeli.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamaPanen , tvBerat, idBarang;
        ImageView iv_foto_panen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaPanen = itemView.findViewById(R.id.tv_judul_jual);
            tvBerat = itemView.findViewById(R.id.tv_qty);
            iv_foto_panen = itemView.findViewById(R.id.iv_jual);
            idBarang = itemView.findViewById(R.id.id_barang);
        }
    }
}
