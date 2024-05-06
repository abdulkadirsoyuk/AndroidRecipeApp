package com.example.recipeapp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Interface.ItemClickListener;
import com.example.recipeapp.R;

public class YemekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener
{

    public TextView txtYemekAdi;
    public TextView txtMalzemeler;
    public TextView txtYapilis;
    public TextView txtPufNoktalar;
    public TextView txtVideoLinki;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public YemekViewHolder(@NonNull View itemView) {
        super(itemView);
        txtYemekAdi=itemView.findViewById(R.id.yemek_adi);
        txtMalzemeler=itemView.findViewById(R.id.yemek_malzemeler);
        txtYapilis=itemView.findViewById(R.id.yemek_yapilisi);
        txtPufNoktalar=itemView.findViewById(R.id.yemek_puf_nokta);
        txtVideoLinki=itemView.findViewById(R.id.video_izleme);
        imageView=itemView.findViewById(R.id.yemek_resmi);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View v){
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
     //   menu.setHeaderTitle("Eylem Seçin");
       // menu.add(0,0,getAdapterPosition(),"Güncelle");
       // menu.add(0,1,getAdapterPosition(),"Sil");
    }
}