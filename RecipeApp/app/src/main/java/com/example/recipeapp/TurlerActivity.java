package com.example.recipeapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.recipeapp.ViewHolder.TurlerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class TurlerActivity extends AppCompatActivity {

    Button btn_tur_ekle;
    EditText editTurAdi;
    Button btnTurSec;
    Button btnTurYukle;









    public static final int PICK_IMAGE_REQUEST=71;
    Uri kaydetmeUrisi;

    RecyclerView recycler_turler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference turYolu;
    FirebaseStorage storage;
    StorageReference resimYolu;

    String kategoriId="";
    FirebaseRecyclerAdapter<Turler, TurlerViewHolder> adapter;

    Turler yeniTur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_turler);

        database=FirebaseDatabase.getInstance();
        turYolu=database.getReference("Turler");
        storage=FirebaseStorage.getInstance();
        resimYolu=storage.getReference();

        recycler_turler=findViewById(R.id.recycler_turler);
        recycler_turler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_turler.setLayoutManager(layoutManager);

      /*  btn_tur_ekle=findViewById(R.id.btn_tur_ekle);
        btn_tur_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turEklemePenceresiGoster();
            }
        });*/

        if (getIntent()!=null){
            kategoriId=getIntent().getStringExtra("KategoriId");
        }
        if (!kategoriId.isEmpty()){
            turleriYukle(kategoriId);
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Güncelle")){
            turGuncellemePenceresiGoster(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }if (item.getTitle().equals("Sil")){
            //Silme
            turSil(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void turGuncellemePenceresiGoster(final String key, final Turler item) {

        AlertDialog.Builder builder =new AlertDialog.Builder(TurlerActivity.this);
        builder.setTitle("Yeni Kategori Ekle");
        builder.setMessage("Lütfen Bilgileriniz Giriniz");
        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_tur_ekle_penceresi= layoutInflater.inflate(R.layout.yeni_tur_ekleme_penceresi,null);

        editTurAdi=yeni_tur_ekle_penceresi.findViewById(R.id.editTurAdi);
        btnTurSec=yeni_tur_ekle_penceresi.findViewById(R.id.btnTurSec);
        btnTurYukle=yeni_tur_ekle_penceresi.findViewById(R.id.btnTurYukle);

        btnTurSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
        btnTurYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimDegis(item);
            }
        });

        builder.setView(yeni_tur_ekle_penceresi);
        builder.setIcon(R.drawable.ic_action_name);
        builder.setPositiveButton("GÜNCELLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // kategoriyi veritabanında guncelleme
                item.setAd(editTurAdi.getText().toString());
                turYolu.child(key).setValue(item);
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

    private void resimDegis(final Turler item) {
        if (kaydetmeUrisi != null) {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String resimAdi = UUID.randomUUID().toString();
            StorageReference resimDosyasi = resimYolu.child("resimler/" + resimAdi);
            resimDosyasi.putFile(kaydetmeUrisi)
                    .addOnSuccessListener(taskSnapshot -> {
                        mDialog.dismiss();
                        Toast.makeText(TurlerActivity.this, "Resim Başarıyla Güncellendi", Toast.LENGTH_SHORT).show();
                        resimDosyasi.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Resmi veritabanına aktarma
                            item.setResim(uri.toString());

                        });
                    }).addOnFailureListener(e -> {
                        mDialog.dismiss();
                        Toast.makeText(TurlerActivity.this, "Resim Yüklenemedi", Toast.LENGTH_SHORT).show();
                    }).addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        mDialog.setMessage("% " + progress + " Yüklendi");
                    });
        }
    }

    private void turSil(String key) {
        turYolu.child(key).removeValue();
    }

    private void turleriYukle(String kategoriId) {

        Query filtrele= turYolu.orderByChild("kategoriid").equalTo(kategoriId);

        FirebaseRecyclerOptions<Turler> secenekler = new FirebaseRecyclerOptions.Builder<Turler>()
                .setQuery(filtrele, Turler.class).build();
        adapter = new FirebaseRecyclerAdapter<Turler, TurlerViewHolder>(secenekler) {
            @Override
            protected void onBindViewHolder(@NonNull TurlerViewHolder TurlerViewHolder, int i, @NonNull Turler turler) {

                TurlerViewHolder.txtTurAdi.setText(turler.getAd());
                Picasso.with(getBaseContext()).load(turler.getResim()).into(TurlerViewHolder.imageView);

                final Turler tiklandiginda = turler;
                TurlerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // her satıra tıklandığında ne yapsın
                        Intent turler =new Intent(TurlerActivity.this,YemeklerActivity.class);
                        turler.putExtra("TurId",adapter.getRef(position).getKey());
                        startActivity(turler);
                    }
                });


            }

            @NonNull
            @Override
            public TurlerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tur_satiri_ogesi, parent, false);

                return new TurlerViewHolder(itemView);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler_turler.setAdapter(adapter);




    }

    private void turEklemePenceresiGoster() {

        AlertDialog.Builder builder =new AlertDialog.Builder(TurlerActivity.this);
        builder.setTitle("Yeni Tür Ekle");
        builder.setMessage("Lütfen Bilgileriniz Giriniz");
        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_tur_ekle_penceresi= layoutInflater.inflate(R.layout.yeni_tur_ekleme_penceresi,null);

        editTurAdi=yeni_tur_ekle_penceresi.findViewById(R.id.editTurAdi);
        btnTurSec=yeni_tur_ekle_penceresi.findViewById(R.id.btnTurSec);
        btnTurYukle=yeni_tur_ekle_penceresi.findViewById(R.id.btnTurYukle);

        btnTurSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
        btnTurYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
            }
        });

        builder.setView(yeni_tur_ekle_penceresi);
        builder.setIcon(R.drawable.ic_action_name);
        builder.setPositiveButton("EKLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // kategoriyi veritabanına aktarma
                if (yeniTur!=null){
                    turYolu.push().setValue(yeniTur);
                    Toast.makeText(TurlerActivity.this, yeniTur.getAd()+" Türü Başarıyla Eklendi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(TurlerActivity.this, yeniTur.getAd()+" Türü Eklenemedi", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            kaydetmeUrisi=data.getData();
            btnTurSec.setText("SEÇİLDİ");
        }
    }

    private void resimSec() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Resim Seç"),PICK_IMAGE_REQUEST);




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
                        Toast.makeText(TurlerActivity.this, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show();
                        resimDosyasi.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Resmi veritabanına aktarma

                            yeniTur=new Turler(editTurAdi.getText().toString(),uri.toString(),kategoriId);

                        });
                    }).addOnFailureListener(e -> {
                        mDialog.dismiss();
                        Toast.makeText(TurlerActivity.this, "Resim Yüklenemedi", Toast.LENGTH_SHORT).show();
                    }).addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        mDialog.setMessage("% " + progress + " Yüklendi");
                    });
        }
    }
}