package com.example.recipeapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.recipeapp.Model.Kategori;
import com.example.recipeapp.ViewHolder.AdminKategoriViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class AdminMainActivity extends AppCompatActivity {

    Button btn_kategori_ekle;

    EditText editKategoriAdi;
    Button btnSec;
    Button btnYukle;

    public static final int PICK_IMAGE_REQUEST=71;
    Uri kaydetmeUrisi;


    FirebaseDatabase database;
    DatabaseReference kategoriYolu;
    FirebaseStorage storage;
    StorageReference resimYolu;


    FirebaseRecyclerAdapter <Kategori, AdminKategoriViewHolder> adapter;

    RecyclerView recycler_kategori;
    RecyclerView.LayoutManager layoutManager;

    Kategori yeniKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin_main);

            database=FirebaseDatabase.getInstance();
            kategoriYolu=database.getReference("Kategori");
            storage=FirebaseStorage.getInstance();
            resimYolu=storage.getReference();

            recycler_kategori=findViewById(R.id.recycler_kategori);
            recycler_kategori.setHasFixedSize(true);
            layoutManager=new LinearLayoutManager(this);
            recycler_kategori.setLayoutManager(layoutManager);



            btn_kategori_ekle=findViewById(R.id.btn_kategori_ekle1);
            btn_kategori_ekle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kategoriEklePenceresiGoster();
                }
            });

            kategoriYukle();




            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;

            });

        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("deneme" +
                    "", ex.getMessage());
        }

    }

    private void kategoriYukle() {
        try {


            FirebaseRecyclerOptions<Kategori> secenekler = new FirebaseRecyclerOptions.Builder<Kategori>()
                    .setQuery(kategoriYolu, Kategori.class).build();
            adapter = new FirebaseRecyclerAdapter<Kategori, AdminKategoriViewHolder>(secenekler) {
                @Override
                protected void onBindViewHolder(@NonNull AdminKategoriViewHolder AdminKategoriViewHolder, int i, @NonNull Kategori kategori) {

                    AdminKategoriViewHolder.txtKategoriAdi.setText(kategori.getAd());
                    Picasso.with(getBaseContext()).load(kategori.getResim()).into(AdminKategoriViewHolder.imageView);

                    Kategori tiklandiginda = kategori;
                    AdminKategoriViewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            // her satıra tıklandığında ne yapsın
                            Intent turler =new Intent(AdminMainActivity.this,AdminTurlerActivity.class);
                            turler.putExtra("KategoriId",adapter.getRef(position).getKey());
                            startActivity(turler);
                        }
                    });


                }

                @NonNull
                @Override
                public AdminKategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.kategori_satiri_ogesi, parent, false);

                    return new AdminKategoriViewHolder(itemView);
                }
            };

            adapter.startListening();
            adapter.notifyDataSetChanged();
            recycler_kategori.setAdapter(adapter);

        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("deneme" +
                    "", ex.getMessage());
        }
    }


    private void kategoriEklePenceresiGoster() {


        AlertDialog.Builder builder =new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle("Yeni Kategori Ekle");
        builder.setMessage("Lütfen Bilgileriniz Giriniz");
        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_kategori_ekle_penceresi= layoutInflater.inflate(R.layout.yeni_kategori_ekleme_penceresi,null);

        editKategoriAdi=yeni_kategori_ekle_penceresi.findViewById(R.id.editKategoriAdi);
        btnSec=yeni_kategori_ekle_penceresi.findViewById(R.id.btnSec);
        btnYukle=yeni_kategori_ekle_penceresi.findViewById(R.id.btnYukle);

        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
        btnYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
            }
        });

        builder.setView(yeni_kategori_ekle_penceresi);
        builder.setIcon(R.drawable.ic_action_name);
        builder.setPositiveButton("EKLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // kategoriyi veritabanına aktarma
                if (yeniKategori!=null){
                    kategoriYolu.push().setValue(yeniKategori);
                    Toast.makeText(AdminMainActivity.this, yeniKategori.getAd()+" Kategorisi Başarıyla Eklendi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AdminMainActivity.this, yeniKategori.getAd()+" Kategorisi Eklenemedi", Toast.LENGTH_SHORT).show();

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
            btnSec.setText("SEÇİLDİ");
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
                        Toast.makeText(AdminMainActivity.this, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show();
                        resimDosyasi.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Resmi veritabanına aktarma

                            yeniKategori=new Kategori(editKategoriAdi.getText().toString(),uri.toString());

                        });
                    }).addOnFailureListener(e -> {
                        mDialog.dismiss();
                        Toast.makeText(AdminMainActivity.this, "Resim Yüklenemedi", Toast.LENGTH_SHORT).show();
                    }).addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        mDialog.setMessage("% " + progress + " Yüklendi");
                    });
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Güncelle")){
            kategoriGuncellemePenceresiGoster(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }if (item.getTitle().equals("Sil")){
            //Silme
            kategoriSil(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void kategoriGuncellemePenceresiGoster(String key, Kategori item) {

        AlertDialog.Builder builder =new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle("Yeni Kategori Ekle");
        builder.setMessage("Lütfen Bilgileriniz Giriniz");
        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_kategori_ekle_penceresi= layoutInflater.inflate(R.layout.yeni_kategori_ekleme_penceresi,null);

        editKategoriAdi=yeni_kategori_ekle_penceresi.findViewById(R.id.editKategoriAdi);
        btnSec=yeni_kategori_ekle_penceresi.findViewById(R.id.btnSec);
        btnYukle=yeni_kategori_ekle_penceresi.findViewById(R.id.btnYukle);

        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
        btnYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimDegis(item);
            }
        });

        builder.setView(yeni_kategori_ekle_penceresi);
        builder.setIcon(R.drawable.ic_action_name);
        builder.setPositiveButton("GÜNCELLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // kategoriyi veritabanında guncelleme
                item.setAd(editKategoriAdi.getText().toString());
                kategoriYolu.child(key).setValue(item);
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

    private void resimDegis(final Kategori item) {
        if (kaydetmeUrisi != null) {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();
            String resimAdi = UUID.randomUUID().toString();
            StorageReference resimDosyasi = resimYolu.child("resimler/" + resimAdi);
            resimDosyasi.putFile(kaydetmeUrisi)
                    .addOnSuccessListener(taskSnapshot -> {
                        mDialog.dismiss();
                        Toast.makeText(AdminMainActivity.this, "Resim Başarıyla Güncellendi", Toast.LENGTH_SHORT).show();
                        resimDosyasi.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Resmi veritabanına aktarma
                            item.setResim(uri.toString());

                        });
                    }).addOnFailureListener(e -> {
                        mDialog.dismiss();
                        Toast.makeText(AdminMainActivity.this, "Resim Yüklenemedi", Toast.LENGTH_SHORT).show();
                    }).addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        mDialog.setMessage("% " + progress + " Yüklendi");
                    });
        }
    }

    private void kategoriSil(String key) {
        kategoriYolu.child(key).removeValue();
    }
}