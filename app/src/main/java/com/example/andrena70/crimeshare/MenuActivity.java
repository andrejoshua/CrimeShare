package com.example.andrena70.crimeshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrena70.crimeshare.news.JSONRetrieve;

public class MenuActivity extends Activity implements View.OnClickListener {

    public static String SERVER_URI = "http://gsmproduction.net/Services/getnews.php";

    private ImageView buttonList;
    private TextView textList;
    private ImageView buttonInsert;
    private TextView textInsert;
    private ImageView buttonAbout;
    private TextView textAbout;
    private ImageView buttonRate;
    private TextView textRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        JSONRetrieve retrieve = new JSONRetrieve(this);
        retrieve.executeRetrieval(SERVER_URI);

        buttonList = (ImageView) findViewById(R.id.imageViewGetNews);
        buttonList.setOnClickListener(this);
        textList = (TextView) findViewById(R.id.getNewsText);
        textList.setOnClickListener(this);
        buttonInsert = (ImageView) findViewById(R.id.imageViewReportNews);
        buttonInsert.setOnClickListener(this);
        textInsert = (TextView) findViewById(R.id.reportNewsText);
        textInsert.setOnClickListener(this);
        buttonAbout = (ImageView) findViewById(R.id.imageViewAboutUs);
        buttonAbout.setOnClickListener(this);
        textAbout = (TextView) findViewById(R.id.aboutusText);
        textAbout.setOnClickListener(this);
        buttonRate = (ImageView) findViewById(R.id.imageViewRateApp);
        buttonRate.setOnClickListener(this);
        textRate = (TextView) findViewById(R.id.rateAppText);
        textRate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewGetNews:
            case R.id.getNewsText:
                Intent intentList = new Intent(MenuActivity.this, ListActivity.class);
                startActivity(intentList);
                break;
            case R.id.imageViewReportNews:
            case R.id.reportNewsText:
                Intent intentInsert = new Intent(MenuActivity.this, InsertActivity.class);
                startActivity(intentInsert);
                break;
            case R.id.imageViewAboutUs:
            case R.id.aboutusText:
                Intent intentAbout = new Intent(MenuActivity.this, AboutUsActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.imageViewRateApp:
            case R.id.rateAppText:
                Intent intentRate = new Intent(MenuActivity.this, RateAppActivity.class);
                startActivity(intentRate);
                break;
            default:
                break;
        }
    }
}