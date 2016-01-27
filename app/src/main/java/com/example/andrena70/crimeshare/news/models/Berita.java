package com.example.andrena70.crimeshare.news.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by andre.na70 on 12/26/2015.
 */
public class Berita {
    private String idberita;
    private String headline;
    private Date waktu;
    private String pelapor;
    private String deskripsi;
    private float longitude;
    private float latitude;
    private String foto;
    private String foto_64;

    public Berita() {

    }

    public Berita(String headline, Date waktu, String pelapor, String deskripsi, float longitude, float latitude, String foto, String foto_64) {
        super();
        this.headline = headline;
        this.waktu = waktu;
        this.pelapor = pelapor;
        this.deskripsi = deskripsi;
        this.longitude = longitude;
        this.latitude = latitude;
        this.foto = foto;
        this.foto_64 = foto_64;
    }

    public Berita(String _idberita, String _headline, Date _waktu, String _pelapor, String _deskripsi, float _longitude, float _latitude, String _foto, String _foto_64) {
        super();
        this.idberita = _idberita;
        this.headline = _headline;
        this.waktu = _waktu;
        this.pelapor = _pelapor;
        this.deskripsi = _deskripsi;
        this.longitude = _longitude;
        this.latitude = _latitude;
        this.foto = _foto;
        this.foto_64 = _foto_64;
    }

    public String getIdberita() {
        return idberita;
    }

    public String getHeadline() {
        return headline;
    }

    public String getWaktu() {
        SimpleDateFormat fwaktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String swaktu = fwaktu.format(this.waktu);
        return swaktu;
    }

    public String getPelapor() {
        return pelapor;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getFoto() {
        return foto;
    }

    public String getFoto_64() { return foto_64; }

    public void setIdberita(String idberita) {
        this.idberita = idberita;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setWaktu(String swaktu) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            this.waktu = formatter.parse(swaktu);
        } catch (ParseException e) {
            this.waktu = null;
            e.printStackTrace();
        }
    }

    public void setPelapor(String pelapor) {
        this.pelapor = pelapor;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        SimpleDateFormat fwaktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String swaktu = fwaktu.format(this.waktu);
        return "Berita [idberita=" + this.idberita + ", headline=" + this.headline + ", waktu=" + swaktu + ", pelapor=" + this.pelapor + ", deskripsi=" + this.deskripsi + ", longitude=" + Float.toString(this.longitude) + ", latitude=" + Float.toString(this.latitude) + ", foto=" + this.foto.toString() + "]";
    }
}
