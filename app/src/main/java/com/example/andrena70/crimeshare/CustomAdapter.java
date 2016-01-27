package com.example.andrena70.crimeshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andrena70.crimeshare.news.ListPair;
import com.example.andrena70.crimeshare.news.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by andre.na70 on 12/26/2015.
 */
public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final List<ListPair> databerita;

    private static LayoutInflater inflater = null;

    public CustomAdapter(ListActivity mainActivity, List<ListPair> arrayBerita) {
        this.context = mainActivity;
        this.databerita = arrayBerita;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return databerita.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        public TextView headlinietv;
        public TextView waktutv;
        public TextView readmoretv;
        public ImageButton deletetv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final ListPair currberita = databerita.get(position);

        View rowView;
        rowView = inflater.inflate(R.layout.list_item, parent, false);
        holder.headlinietv = (TextView) rowView.findViewById(R.id.textview_part_headline);
        holder.waktutv = (TextView) rowView.findViewById(R.id.textview_part_date);
        holder.readmoretv = (TextView) rowView.findViewById(R.id.textview_part_more);
        holder.deletetv = (ImageButton) rowView.findViewById(R.id.button_part_delete);

        holder.headlinietv.setText(currberita.headline);

        SimpleDateFormat fwaktu = new SimpleDateFormat("dd-MM-yyyy");
        String swaktu = fwaktu.format(currberita.tanggal);
        holder.waktutv.setText(swaktu);

        holder.readmoretv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DetailActivity.class);
                intent.putExtra("id_berita", currberita.idberita);
                context.startActivity(intent);
            }
        });

        holder.deletetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLHelper db = new SQLHelper(context.getApplicationContext(), ListActivity.DB_VERSION);
                db.deleteData(currberita.idberita);

                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

        return rowView;
    }
}
