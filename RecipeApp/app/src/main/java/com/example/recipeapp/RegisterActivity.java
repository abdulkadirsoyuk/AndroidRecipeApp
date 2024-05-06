package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipeapp.Model.Kullanici;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterActivity extends AppCompatActivity {
    Button btnUyeOl;
    EditText txtAd, txtSoyad, txtKullaniciAdi, txtSifre;

    FirebaseDatabase database;
    DatabaseReference kullanici;
    Kullanici kullanicilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        btnUyeOl = findViewById(R.id.buttonUyeOl);
        txtAd = findViewById(R.id.editTextAd);
        txtSoyad = findViewById(R.id.editTextSoyad);
        txtKullaniciAdi = findViewById(R.id.editTextKullaniciAdi);
        txtSifre = findViewById(R.id.editTextSifreUyeOl);

        database=FirebaseDatabase.getInstance();
        kullanici=database.getReference("Kullanicilar");

        btnUyeOl.setOnClickListener(v -> {
            // Kullanıcıdan alınan bilgileri kullanarak yeni bir kullanıcı nesnesi oluşturun
            String ad = txtAd.getText().toString();
            String soyad = txtSoyad.getText().toString();
            String kullaniciAdi = txtKullaniciAdi.getText().toString();
            String sifre = txtSifre.getText().toString();

            // Değerlerin boş olup olmadığını kontrol edin
            if (TextUtils.isEmpty(ad) || TextUtils.isEmpty(soyad) || TextUtils.isEmpty(kullaniciAdi) || TextUtils.isEmpty(sifre)) {
                // Eğer herhangi bir alan boşsa kullanıcıya uyarı ver
                Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            } else {
                // Kullanıcı nesnesini oluşturun
                Kullanici yeniKullanici = new Kullanici(ad, soyad, kullaniciAdi, sifre);

                // Veritabanına yeni kullanıcıyı ekleyin
                kullanici.push().setValue(yeniKullanici);

                // LoginActivity'e geçiş yap
                Toast.makeText(RegisterActivity.this, "Üye Olma İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}