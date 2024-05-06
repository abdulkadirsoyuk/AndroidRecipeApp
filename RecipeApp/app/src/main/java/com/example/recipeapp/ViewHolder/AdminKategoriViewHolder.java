package com.example.recipeapp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Interface.ItemClickListener;
import com.example.recipeapp.MainActivity;
import com.example.recipeapp.R;

public class AdminKategoriViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener
{

    public TextView txtKategoriAdi;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public AdminKategoriViewHolder(@NonNull View itemView) {
        super(itemView);
        txtKategoriAdi=itemView.findViewById(R.id.kategori_adi);
        imageView=itemView.findViewById(R.id.kategori_resmi);
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
        menu.setHeaderTitle("Eylem Seçin");
         menu.add(0,0,getAdapterPosition(),"Güncelle");
        menu.add(0,1,getAdapterPosition(),"Sil");
    }
}
