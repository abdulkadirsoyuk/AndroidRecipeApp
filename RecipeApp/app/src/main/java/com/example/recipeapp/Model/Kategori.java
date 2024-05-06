package com.example.recipeapp.Model;

public class Kategori {

    private String ad;
    private String resim;

    // Parametresiz yapılandırıcı eklendi
    public Kategori() {
        // Boş yapılandırıcı
    }

    public Kategori(String ad, String resim) {
        this.ad = ad;
        this.resim = resim;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
