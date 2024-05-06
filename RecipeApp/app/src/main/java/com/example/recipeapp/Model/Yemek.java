package com.example.recipeapp.Model;


public class Yemek {
    private String yemekAdi;
    private String malzemeler;
    private String yapilisi;
    private String pufNokta;
    private String izlemeLinki;
    private String turId;
    private String resim;


    public Yemek(String yemekAdi, String malzemeler, String yapilisi, String pufNokta, String izlemeLinki, String turId, String resim) {
        this.yemekAdi = yemekAdi;
        this.malzemeler = malzemeler;
        this.yapilisi = yapilisi;
        this.pufNokta = pufNokta;
        this.izlemeLinki = izlemeLinki;
        this.turId = turId;
        this.resim = resim;
    }

    public Yemek() {
    }

    public String getYemekAdi() {
        return yemekAdi;
    }

    public void setYemekAdi(String yemekAdi) {
        this.yemekAdi = yemekAdi;
    }

    public String getMalzemeler() {
        return malzemeler;
    }

    public void setMalzemeler(String malzemeler) {
        this.malzemeler = malzemeler;
    }

    public String getYapilisi() {
        return yapilisi;
    }

    public void setYapilisi(String yapilisi) {
        this.yapilisi = yapilisi;
    }

    public String getPufNokta() {
        return pufNokta;
    }

    public void setPufNokta(String pufNokta) {
        this.pufNokta = pufNokta;
    }

    public String getIzlemeLinki() {
        return izlemeLinki;
    }

    public void setIzlemeLinki(String izlemeLinki) {
        this.izlemeLinki = izlemeLinki;
    }

    public String getTurId() {
        return turId;
    }

    public void setTurId(String turId) {
        this.turId = turId;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
