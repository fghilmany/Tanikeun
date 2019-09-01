package com.example.tanikeun;

public class JualBeli {

    String nama_panen,url_foto_panen;
    Integer berat;
    String  id_barang;

    public JualBeli() {
    }

    public String getNama_panen() {
        return nama_panen;
    }

    public void setNama_panen(String nama_panen) {
        this.nama_panen = nama_panen;
    }

    public Integer getBerat() {
        return berat;
    }

    public void setBerat(Integer berat) {
        this.berat = berat;
    }

    public String getUrl_foto_panen() {
        return url_foto_panen;
    }

    public void setUrl_foto_panen(String url_foto_panen) {
        this.url_foto_panen = url_foto_panen;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }
}
