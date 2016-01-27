package com.example.andrena70.crimeshare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrena70.crimeshare.maps.GPSListener;
import com.example.andrena70.crimeshare.news.SQLHelper;
import com.example.andrena70.crimeshare.news.models.Berita;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InsertActivity extends Activity {

    private EditText headlineEdit;
    private EditText pelaporEdit;
    private EditText deskripsiEdit;
    private EditText longitudeEdit;
    private EditText latitudeEdit;
    private ImageView fotoEdit;
    private Button buttonInsert;
    private Button buttonFoto;
    private Button buttonPosisi;

    private String fotoNamaFile = null;
    private String fotoNamaFolder = null;
    private Bitmap bitmapFile = null;
    private String bitmapFileDir = "img/";

    private LocationManager locationManager;
    private LocationListener gpsListener;
    private Double currLong = 0.0;
    private Double currLat = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        headlineEdit = (EditText) findViewById(R.id.edittext_insert_headline);
        pelaporEdit = (EditText) findViewById(R.id.edittext_insert_pelapor);
        deskripsiEdit = (EditText) findViewById(R.id.edittext_insert_deskripsi);
        longitudeEdit = (EditText) findViewById(R.id.edittext_insert_longitude);
        latitudeEdit = (EditText) findViewById(R.id.edittext_insert_latitude);
        fotoEdit = (ImageView) findViewById(R.id.imageview_insert_foto);
        buttonInsert = (Button) findViewById(R.id.button_insert);
        buttonFoto = (Button) findViewById(R.id.button_insert_foto);
        buttonPosisi = (Button) findViewById(R.id.button_getkoordinat);

        longitudeEdit.setText(Double.toString(currLong));
        latitudeEdit.setText(Double.toString(currLat));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new GPSListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission location not granted", Toast.LENGTH_LONG).show();
            finish();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsListener);

        buttonPosisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission location not granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location.getLatitude() != currLat && location.getLongitude() != currLong){
                    longitudeEdit.setText(Double.toString(location.getLongitude()));
                    latitudeEdit.setText(Double.toString(location.getLatitude()));
                    currLat = location.getLatitude();
                    currLong = location.getLongitude();
                }
            }
        });

        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TakePhotoActivity.class);
                startActivityForResult(i, 10);
            }
        });

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline = null;
                if (headlineEdit.getText() != null) {
                    headline = headlineEdit.getText().toString();
                }
                String pelapor = null;
                if (pelaporEdit.getText() != null) {
                    pelapor = pelaporEdit.getText().toString();
                }
                String deskripsi = null;
                if (deskripsiEdit.getText() != null) {
                    deskripsi = deskripsiEdit.getText().toString();
                }
                float longitude = 0.0f;
                if (longitudeEdit.getText().toString().length() > 0 ) {
                    longitude = Float.parseFloat(longitudeEdit.getText().toString());
                }
                float latitude = 0.0f;
                if (latitudeEdit.getText().toString().length() > 0) {
                    latitude = Float.parseFloat(latitudeEdit.getText().toString());
                }

                String foto_64 = "";
                String foto = bitmapFileDir;
                if (fotoEdit.getDrawable() != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();

                    foto_64 = Base64.encodeToString(b, Base64.DEFAULT);
                    foto = bitmapFileDir + fotoNamaFile;
                }

                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                Date currTime;
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    currTime = formatter.parse(currentDate);
                } catch (ParseException e) {
                    currTime = null;
                    e.printStackTrace();
                }

                Berita berita = new Berita(headline, currTime, pelapor, deskripsi, longitude, latitude, foto, foto_64);

                SQLHelper db = new SQLHelper(getApplicationContext(), ListActivity.DB_VERSION);

                db.tambahBerita(berita);

                Toast.makeText(getApplicationContext(), "Added to database on " + currentDate, Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "No data came back", Toast.LENGTH_LONG).show();
                }
                else{
                    Bundle extras = data.getExtras();
                    String[] results = extras.getStringArray("RESULT");

                    this.fotoNamaFolder = results[0];
                    this.fotoNamaFile = results[1];

                    File imgFile = new File("/sdcard/" + this.fotoNamaFolder + "/" + this.fotoNamaFile);

                    if (imgFile.exists()) {
                        Bitmap fileBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        fotoEdit.setImageBitmap(fileBitmap);
                        this.bitmapFile = fileBitmap.copy(fileBitmap.getConfig(), true);
                    }
                }
            }
        }
    }
}