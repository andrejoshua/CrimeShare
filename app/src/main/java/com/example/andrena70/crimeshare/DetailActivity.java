package com.example.andrena70.crimeshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrena70.crimeshare.news.SQLHelper;
import com.example.andrena70.crimeshare.news.models.Berita;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends Activity {

    private TextView headlineText;
    private TextView tanggalText;
    private TextView deskripsiText;
    private TextView pelaporText;
    private ImageView fotoImage;
    private TextView posisiText;

    private String idberita = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.idberita = extras.getString("id_berita");
        }

        headlineText = (TextView) findViewById(R.id.textview_detail_headline);
        tanggalText = (TextView) findViewById(R.id.textview_detail_date);
        deskripsiText = (TextView) findViewById(R.id.textview_detail_description);
        pelaporText = (TextView) findViewById(R.id.textview_detail_reporter);
        fotoImage = (ImageView) findViewById(R.id.imageview_detail_foto);
        posisiText = (TextView) findViewById(R.id.textview_detail_posisi);

        SQLHelper db = new SQLHelper(this, ListActivity.DB_VERSION);

        Berita berita = db.getDetailBerita(this.idberita);

        headlineText.setText(berita.getHeadline());
        tanggalText.setText(berita.getWaktu());
        deskripsiText.setText(berita.getDeskripsi());
        pelaporText.setText(berita.getPelapor());

        final double latitude = (double) berita.getLatitude();
        final double longitude = (double) berita.getLongitude();

        String cityName = null;
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String sentCityName = cityName;

        String posisiString = "Posisi (kota): " + cityName + ".";

        posisiText.setText(posisiString);

        posisiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                double[] ll = new double[] {latitude, longitude};
                Bundle extras = new Bundle();
                extras.putDoubleArray("Locs", ll);
                extras.putString("City", sentCityName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        String imageBase64 = berita.getFoto_64();
        byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        fotoImage.setImageBitmap(decodedByte);
    }
}
