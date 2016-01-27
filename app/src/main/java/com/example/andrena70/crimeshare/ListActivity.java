package com.example.andrena70.crimeshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrena70.crimeshare.news.ListPair;
import com.example.andrena70.crimeshare.news.SQLHelper;

import java.util.List;

public class ListActivity extends Activity {

    private ListView listView;
    private List<ListPair> dataBerita;

    public static int DB_VERSION = 2;
    public static boolean needToRestart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listview_berita);

        View listHeader = getLayoutInflater().inflate(R.layout.list_header, null);
        TextView insertView = (TextView) listHeader.findViewById(R.id.textview_header_add);

        insertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

        listView.addHeaderView(listHeader);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SQLHelper db = new SQLHelper(this, DB_VERSION);

        this.dataBerita = db.getSemuaBeritaSebagian();
        if (this.dataBerita == null) {
            Toast.makeText(getApplicationContext(), "No data in database", Toast.LENGTH_LONG).show();
        }

        listView.setAdapter(new CustomAdapter(this, this.dataBerita));
    }
}
