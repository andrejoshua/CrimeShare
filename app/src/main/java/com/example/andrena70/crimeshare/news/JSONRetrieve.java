package com.example.andrena70.crimeshare.news;

import android.app.ProgressDialog;
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
import java.util.Scanner;

/**
 * Created by andre.na70 on 1/6/2016.
 */
public class JSONRetrieve {

    private MenuActivity activity;
    private ProgressDialog progressDialog;

    public JSONRetrieve(MenuActivity activity) {
        this.activity = activity;
    }

    public void executeRetrieval(String _url) {
        progressDialog = ProgressDialog.show(activity, "Integrating Database...", "Please wait for a moment", true, true);
        String bufferResult = null;

        BufferedReader bufferedReader;
        try {
            java.net.URL url = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json+"");
            }

            bufferResult = sb.toString().trim();

        } catch(Exception e){
            e.printStackTrace();
            Toast.makeText(activity, "Failed integrating database, json not retrieved correctly", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        } finally {
            List<JSONArray> list = new ArrayList<>();

            if (bufferResult == null){
                Toast.makeText(activity, "Buffer result is null", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            Scanner scan = new Scanner(bufferResult);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                try {
                    JSONArray temp = new JSONArray(line);
                    list.add(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Failed integrating database, json not formatted correctly", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
            }

            SQLHelper helper = new SQLHelper(activity, ListActivity.DB_VERSION);
            try {
                helper.synchronizeData(list);
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(activity, "Failed integrating database, json not formatted correctly", Toast.LENGTH_LONG).show();
            } finally {
                progressDialog.dismiss();
                Toast.makeText(activity, "Database integrated", Toast.LENGTH_LONG).show();
            }
        }
    }
}
