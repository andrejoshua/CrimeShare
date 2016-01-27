package com.example.andrena70.crimeshare.news;

import java.util.Date;

/**
 * Created by andre.na70 on 12/26/2015.
 */
public class ListPair {
    public String idberita;
    public String headline;
    public Date tanggal;

    public ListPair(String _idberita, String _headline, Date _tanggal) {
        idberita = _idberita;
        headline = _headline;
        tanggal = _tanggal;
    }
}