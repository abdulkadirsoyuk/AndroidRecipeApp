package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipeapp.Model.Kullanici;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button btnGirisYap;
    EditText txtKullaniciAdi, txtSifre;
    TextView txtUyeOl;

    FirebaseDatabase database;
    DatabaseReference kullanici;
    DatabaseReference admin;
    Kullanici kullanicilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btnGirisYap=findViewById(R.id.buttonGirisYap);
        txtKullaniciAdi=findViewById(R.id.editTextKullanici);
        txtSifre=findViewById(R.id.editTextPassword);
        txtUyeOl=findViewById(R.id.textViewUyeOlma);

        database= FirebaseDatabase.getInstance();
        kullanici=database.getReference("Kullanicilar");
        admin=database.getReference("Admin");

        btnGirisYap.setOnClickListener(v -> {
            String kullaniciAdi = txtKullaniciAdi.getText().toString().trim();
            String sifre = txtSifre.getText().toString().trim();

            if (!TextUtils.isEmpty(kullaniciAdi) && !TextUtils.isEmpty(sifre)) {
                // Firebase veritabanından kullanıcıyı kontrol edin





                kullanici.orderByChild("kullaniciAdi").equalTo(kullaniciAdi).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Kullanıcı adı veritabanında bulundu, şimdi şifreyi kontrol edin
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                                if (kullanici != null && kullanici.getSifre().equals(sifre)) {
                                    // Şifre doğru, giriş yap
                                    Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    return;
                                }
                            }
                            // Şifre yanlış, kullanıcıya bir hata mesajı göster
                            Toast.makeText(LoginActivity.this, "Şifre yanlış, lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
                        } else {
                            // Kullanıcı adı yanlış, lütfen tekrar deneyin
                            admin.orderByChild("kullaniciAdi").equalTo(kullaniciAdi).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Kullanıcı adı veritabanında bulundu, şimdi şifreyi kontrol edin
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Kullanici kullanici = snapshot.getValue(Kullanici.class);
                                            if (kullanici != null && kullanici.getSifre().equals(sifre)) {
                                                // Şifre doğru, giriş yap
                                                Toast.makeText(LoginActivity.this, "Admin Girişi Başarılı", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                                startActivity(intent);
                                                return;
                                            }
                                        }
                                        // Şifre yanlış, kullanıcıya bir hata mesajı göster
                                        Toast.makeText(LoginActivity.this, "Şifre yanlış, lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Kullanıcı adı yanlış, lütfen tekrar deneyin
                                        Toast.makeText(LoginActivity.this, "Kullanıcı adı yanlış, lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Veritabanı erişimi iptal edildi, kullanıcıya bir hata mesajı göster
                                    Toast.makeText(LoginActivity.this, "Veritabanı erişimi iptal edildi", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Veritabanı erişimi iptal edildi, kullanıcıya bir hata mesajı göster
                        Toast.makeText(LoginActivity.this, "Veritabanı erişimi iptal edildi", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // Kullanıcı adı veya şifre boş ise kullanıcıya bir uyarı göster
                Toast.makeText(LoginActivity.this, "Kullanıcı adı ve şifre gereklidir", Toast.LENGTH_SHORT).show();
            }


        });

        txtUyeOl.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}