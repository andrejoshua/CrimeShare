package com.example.andrena70.crimeshare.news;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.example.andrena70.crimeshare.news.models.Berita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;

/**
 * Created by andre.na70 on 12/26/2015.
 */
public class SQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FP_Mobile";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLHelper(Context context, int dbVersion) {
        super(context, DATABASE_NAME, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BERITA = "CREATE TABLE berita (idberita VARCHAR(24) PRIMARY KEY, headline VARCHAR(100), waktu TIMESTAMP, pelapor VARCHAR(50), deskripsi TEXT, longitude VARCHAR(15), latitude VARCHAR(15), foto VARCHAR(100), foto_64 TEXT)";
        db.execSQL(CREATE_BERITA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS berita");

        this.onCreate(db);
    }

    private static final String TABLE_BERITA = "berita";
    private static final String KEY_ID_BERITA = "idberita";
    private static final String KEY_HEADLINE = "headline";
    private static final String KEY_WAKTU = "waktu";
    private static final String KEY_PELAPOR = "pelapor";
    private static final String KEY_DESKRIPSI = "deskripsi";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_FOTO = "foto";
    private static final String KEY_FOTO_64 = "foto_64";

    private static final String[] COLUMNS = {KEY_ID_BERITA, KEY_HEADLINE, KEY_HEADLINE, KEY_WAKTU, KEY_PELAPOR, KEY_DESKRIPSI, KEY_LONGITUDE, KEY_LATITUDE, KEY_FOTO, KEY_FOTO_64};

    public void tambahBerita(Berita berita) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO berita(idberita, headline, waktu, pelapor, deskripsi, longitude, latitude, foto, foto_64) VALUES(?,?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();

        String idberita = generateUniqueId(Integer.toString(new Random().nextInt(10)), true);

        insertStatement.bindString(1, idberita);
        insertStatement.bindString(2, berita.getHeadline());
        insertStatement.bindString(3, berita.getWaktu());
        insertStatement.bindString(4, berita.getPelapor());
        insertStatement.bindString(5, berita.getDeskripsi());
        insertStatement.bindString(6, Float.toString(berita.getLongitude()));
        insertStatement.bindString(7, Float.toString(berita.getLatitude()));
        insertStatement.bindString(8, berita.getFoto());
        insertStatement.bindString(9, berita.getFoto_64());
        insertStatement.executeInsert();

        db.close();
    }

    public void updateBerita(Berita berita) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_HEADLINE, berita.getHeadline());
        cv.put(KEY_WAKTU, berita.getWaktu());
        cv.put(KEY_PELAPOR, berita.getPelapor());
        cv.put(KEY_DESKRIPSI, berita.getDeskripsi());
        cv.put(KEY_LONGITUDE, Float.toString(berita.getLongitude()));
        cv.put(KEY_LATITUDE, Float.toString(berita.getLatitude()));
        cv.put(KEY_FOTO, berita.getFoto());
        cv.put(KEY_FOTO_64, berita.getFoto_64());

        db.update(TABLE_BERITA, cv, "idberita="+berita.getIdberita(), null);
    }


    public Berita getDetailBerita(String _idberita) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BERITA, COLUMNS, "idberita = ?", new String[]{_idberita}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            String idberita = cursor.getString(cursor.getColumnIndex("idberita"));
            String headline = cursor.getString(cursor.getColumnIndex("headline"));
            String pelapor = cursor.getString(cursor.getColumnIndex("pelapor"));
            String deskripsi = cursor.getString(cursor.getColumnIndex("deskripsi"));
            float longitude = cursor.getFloat(cursor.getColumnIndex("longitude"));
            float latitude = cursor.getFloat(cursor.getColumnIndex("latitude"));
            String foto = cursor.getString(cursor.getColumnIndex("foto"));
            String foto_64 = cursor.getString(cursor.getColumnIndex("foto_64"));
            String rawtime = cursor.getString(cursor.getColumnIndex("waktu"));
            Date waktu;
            try {
                waktu = parseDate(rawtime, "yyyy-MM-dd HH:mm:ss");
            } catch (ParseException e){
                waktu = null;
                e.printStackTrace();
            }

            Berita berita = new Berita(idberita, headline, waktu, pelapor, deskripsi, longitude, latitude, foto, foto_64);
            return  berita;
        }
        else {
            return null;
        }
    }

    public List<Berita> getSemuaBeritaLengkap() {
        List<Berita> data = new LinkedList<>();

        String query = "SELECT * FROM " + TABLE_BERITA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Berita berita = null;
        if (cursor.moveToFirst()) {
            do {
                String idberita = cursor.getString(cursor.getColumnIndex("idberita"));
                String headline = cursor.getString(cursor.getColumnIndex("headline"));
                String pelapor = cursor.getString(cursor.getColumnIndex("pelapor"));
                String deskripsi = cursor.getString(cursor.getColumnIndex("deskripsi"));
                float longitude = cursor.getFloat(cursor.getColumnIndex("longitude"));
                float latitude = cursor.getFloat(cursor.getColumnIndex("latitude"));
                String foto = cursor.getString(cursor.getColumnIndex("foto"));
                String foto_64 = cursor.getString(cursor.getColumnIndex("foto_64"));
                String rawtime = cursor.getString(cursor.getColumnIndex("waktu"));
                Date waktu;
                try {
                    waktu = parseDate(rawtime, "yyyy-MM-dd HH:mm:ss");
                } catch (ParseException e){
                    waktu = null;
                    e.printStackTrace();
                }

                berita = new Berita(idberita, headline, waktu, pelapor, deskripsi, longitude, latitude, foto, foto_64);

                data.add(berita);
            }while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return data;
    }

    public List<ListPair> getSemuaBeritaSebagian() {
        List<ListPair> data = new LinkedList<>();

        String query = "SELECT idberita, headline, waktu FROM " + TABLE_BERITA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ListPair berita = null;
        if (cursor.moveToFirst()) {
            do {
                String idberita = cursor.getString(cursor.getColumnIndex("idberita"));
                String headline = cursor.getString(cursor.getColumnIndex("headline"));
                String rawtime = cursor.getString(cursor.getColumnIndex("waktu"));
                Date waktu;
                try {
                    waktu = parseDate(rawtime, "yyyy-MM-dd HH:mm:ss");
                } catch (ParseException e){
                    waktu = null;
                    e.printStackTrace();
                }

                berita = new ListPair(idberita, headline, waktu);

                data.add(berita);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return data;
    }

    public void deleteData(String _idberita) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM berita WHERE idberita = '" + _idberita + "'";

        db.execSQL(query);
    }

    public void synchronizeData(List<JSONArray> _data) throws JSONException {
        for(JSONArray j : _data)
        {
            JSONObject obj = j.getJSONObject(0);
            String idberita = obj.getString("id");
            String headline = obj.getString("head");
            String tanggal = obj.getString("tgl");
            Date currTime;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                currTime = formatter.parse(tanggal);
            } catch (ParseException e) {
                currTime = null;
                e.printStackTrace();
            }
            String pelapor = obj.getString("lapor");
            String deskripsi = obj.getString("deskrip");
            String latitude = obj.getString("lat");
            float latitude_f = Float.parseFloat(latitude);
            String longitude = obj.getString("long");
            float longitude_f = Float.parseFloat(longitude);
            String foto = obj.getString("img");
            String foto_64 = obj.getString("img64");
            Berita data = new Berita(idberita, headline, currTime, pelapor, deskripsi, longitude_f, latitude_f, foto, foto_64);

            Berita berita = getDetailBerita(idberita);
            if(berita==null)
            {
                tambahBerita(data);
            }
            else {
                updateBerita(data);
            }
        }
    }

    private Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }

    private String generateUniqueId(String prefix, boolean more_entropy) {
        long time = System.currentTimeMillis();

        String uniqid = "";
        if (!more_entropy) {
            uniqid = String.format("%s%08x%05x", prefix, time/1000, time);
        }
        else {
            SecureRandom sec = new SecureRandom();
            byte[] sbuf = sec.generateSeed(8);
            ByteBuffer bb = ByteBuffer.wrap(sbuf);

            uniqid = String.format("%s%08x%05x", prefix, time/1000, time);
            uniqid += "." + String.format("%.8s", ""+bb.getLong()*-1);
        }

        return uniqid;
    }
}
