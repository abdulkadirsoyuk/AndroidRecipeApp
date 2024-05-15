package com.example.recipeapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Interface.ItemClickListener;
import com.example.recipeapp.Model.Turler;
import com.example.recipeapp.Model.Yemek;
import com.example.recipeapp.ViewHolder.TurlerViewHolder;
import com.example.recipeapp.ViewHolder.YemekViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AdminYemeklerActivity extends AppCompatActivity {

    Button btn_yemek_ekle;
    EditText editYemekAdi;
    EditText editMalzemeler;
    EditText editYapilisi;
    EditText editPufNokta;
    EditText editIzlemeLinki;
    Button btnYemekSec;
    Button btnYemekYukle;
    RecyclerView recycler_yemekler;
    RecyclerView.LayoutManager layoutManager;

    public static final int PICK_IMAGE_REQUEST=71;

    Uri kaydetmeUrisi;

    FirebaseDatabase database;
    DatabaseReference yemekYolu;
    FirebaseStorage storage;
    StorageReference resimYolu;

    String turId="";





    FirebaseRecyclerAdapter<Yemek, YemekViewHolder> adapter;
    Yemek yeniYemek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_yemekler);

        database = FirebaseDatabase.getInstance();
        yemekYolu = database.getReference("Yemekler");
        storage = FirebaseStorage.getInstance();
        resimYolu = storage.getReference();
        recycler_yemekler = findViewById(R.id.recycler_yemekler);
        recycler_yemekler.setHasFixedSize(true);
        /*LinearLayoutManager */ layoutManager = new LinearLayoutManager(this);
        recycler_yemekler.setLayoutManager(layoutManager);

        btn_yemek_ekle = findViewById(R.id.btn_yemek_ekle);
        btn_yemek_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekEklemePenceresiGoster();
            }
        });
        if (getIntent() != null) {
            turId = getIntent().getStringExtra("TurId");
        }
        if (!turId.isEmpty()) {
            yemekleriYukle(turId);
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void yemekleriYukle(String turId) {
        Query filtrele = yemekYolu.orderByChild("turId").equalTo(turId);

        FirebaseRecyclerOptions<Yemek> secenekler = new FirebaseRecyclerOptions.Builder<Yemek>()
                .setQuery(filtrele, Yemek.class).build();
        try {


            adapter = new FirebaseRecyclerAdapter<Yemek, YemekViewHolder>(secenekler) {
                @Override
                protected void onBindViewHolder(@NonNull YemekViewHolder yemekViewHolder, int i, @NonNull Yemek yemek) {

                    yemekViewHolder.txtYemekAdi.setText(yemek.getYemekAdi());
                    yemekViewHolder.txtMalzemeler.setText(yemek.getMalzemeler());
                    yemekViewHolder.txtYapilis.setText(yemek.getYapilisi());
                    yemekViewHolder.txtPufNoktalar.setText(yemek.getPufNokta());
                    Picasso.with(getBaseContext()).load(yemek.getResim()).into(yemekViewHolder.imageView);

                    final Yemek tiklandiginda = yemek;



                }


                @NonNull
                @Override
                public YemekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.yemek_satiri_ogesi, parent, false);

                    return new YemekViewHolder(itemView);
                }
            };
        }
        catch (Exception ex){
            Log.e("adaptorhata",ex.getMessage());
        }

        adapter.startListening();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                // Yeni öğeler eklendiğinde yapılacak işlemler
                Log.d("Adapter Kontrol", "Yeni öğeler eklendi: " + itemCount);
            }



            @Override
            public void onChanged() {
                super.onChanged();
                // Veri seti değiştiğinde yapılacak işlemler
                if (adapter.getItemCount() > 0) {
                    // Veri seti boş değilse recycler_yemekler'e adaptörü ayarla
                    recycler_yemekler.setAdapter(adapter);
                } else {
                    // Veri seti boş ise hata mesajı veya gerekli işlemler yapılabilir
                    Log.e("Adapter Kontrol", "Veri seti boş");
                }
            }
        });



        adapter.notifyDataSetChanged();
        if (adapter != null && adapter.getItemCount() > 0) {
            // Adaptör boş değil ve en az bir öğe içeriyor
            // recycler_yemekler'e adaptörü ayarla
            recycler_yemekler.setAdapter(adapter);
        } else {
            // Adaptör boş veya null
            // Hata mesajı veya gerekli işlemleri yapabilirsiniz
            Log.e("Adaptor Kontrolü", "Adaptör boş veya null");
        }

        try {

            recycler_yemekler.setAdapter(adapter);

        }

        catch (Exception ex){

            Log.e("ornek",ex.getMessage());
        }
    }


    private void yemekEklemePenceresiGoster() {

        AlertDialog.Builder builder =new AlertDialog.Builder(AdminYemeklerActivity.this);
        builder.setTitle("Yeni Yemek Ekle");
        builder.setMessage("Lütfen Bilgileriniz Giriniz");
        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_yemek_ekle_penceresi= layoutInflater.inflate(R.layout.yeni_yemek_ekleme_penceresi,null);

        editYemekAdi=yeni_yemek_ekle_penceresi.findViewById(R.id.editYemekAdi);
        editMalzemeler=yeni_yemek_ekle_penceresi.findViewById(R.id.editMalzemeler);
        editYapilisi=yeni_yemek_ekle_penceresi.findViewById(R.id.editYapilis);
        editPufNokta=yeni_yemek_ekle_penceresi.findViewById(R.id.editPufNoktası);
        btnYemekSec=yeni_yemek_ekle_penceresi.findViewById(R.id.btnYemekSec);
        btnYemekYukle=yeni_yemek_ekle_penceresi.findViewById(R.id.btnYemekYukle);

        btnYemekSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
        btnYemekYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
            }
        });

        builder.setView(yeni_yemek_ekle_penceresi);
        builder.setIcon(R.drawable.ic_action_name);
        builder.setPositiveButton("EKLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // kategoriyi veritabanına aktarma
                if (yeniYemek!=null){
                    yemekYolu.push().setValue(yeniYemek);
                    Toast.makeText(AdminYemeklerActivity.this, yeniYemek.getYemekAdi()+" Yemeği Başarıyla Eklendi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AdminYemeklerActivity.this, yeniYemek.getYemekAdi()+" Yemeği Eklenemedi", Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("VAZGEC", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Sonra Kodlanacak
                dialog.dismiss();
            }
        });
        builder.show();


    }

    private void resimYukle() {
        if (kaydetmeUrisi != null) {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String resimAdi = UUID.randomUUID().toString();
            StorageReference resimDosyasi = resimYolu.child("resimler/" + resimAdi);
            resimDosyasi.putFile(kaydetmeUrisi)
                    .addOnSuccessListener(taskSnapshot -> {
                        mDialog.dismiss();
                        Toast.makeText(AdminYemeklerActivity.this, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show();
                        resimDosyasi.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Resmi veritabanına aktarma

                            yeniYemek=new Yemek(editYemekAdi.getText().toString(),editMalzemeler.getText().toString(),
                                    editYapilisi.getText().toString(),
                                    editPufNokta.getText().toString(),
                                    turId,uri.toString());

                        });
                    }).addOnFailureListener(e -> {
                        mDialog.dismiss();
                        Toast.makeText(AdminYemeklerActivity.this, "Resim Yüklenemedi", Toast.LENGTH_SHORT).show();
                    }).addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        mDialog.setMessage("% " + progress + " Yüklendi");
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            kaydetmeUrisi=data.getData();
            btnYemekSec.setText("SEÇİLDİ");
        }
    }
    private void resimSec() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Resim Seç"),PICK_IMAGE_REQUEST);
    }
}