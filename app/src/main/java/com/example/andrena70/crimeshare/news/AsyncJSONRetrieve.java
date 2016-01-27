package com.example.andrena70.crimeshare.news;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.andrena70.crimeshare.ListActivity;
import com.example.andrena70.crimeshare.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre.na70 on 1/6/2016.
 */
public class AsyncJSONRetrieve extends AsyncTask<Void, Void, List<JSONArray>> {
    private MenuActivity activity;
    private String URI;

    private ProgressDialog loading;

    public AsyncJSONRetrieve(MenuActivity _activity, String _uri) {
        this.activity = _activity;
        this.URI = _uri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(activity, "Loading...", "Waiting for integrate database", true, true);
    }

    @Override
    protected void onPostExecute(List<JSONArray> jsonArrays) {
        super.onPostExecute(jsonArrays);
        SQLHelper sqlHelper = new SQLHelper(this.activity.getApplicationContext(), ListActivity.DB_VERSION);
        try {
            if (jsonArrays != null) {
                sqlHelper.synchronizeData(jsonArrays);
                Toast.makeText(this.activity.getApplicationContext(), "Database integrated", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this.activity.getApplicationContext(), "Database not integrated", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading.dismiss();
    }

    @Override
    protected List<JSONArray> doInBackground(Void... voids) {
        String uri = this.URI;

        BufferedReader bufferedReader;
        try {
            java.net.URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;

            List<JSONArray> list = new ArrayList<>();
            while((json = bufferedReader.readLine())!= null){
                JSONArray temp = new JSONArray(json);
                list.add(temp);
            }

            return list;

        }catch(Exception e){
            return null;
        }
    }
}
