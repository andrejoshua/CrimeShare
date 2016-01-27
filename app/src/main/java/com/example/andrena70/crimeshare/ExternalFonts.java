package com.example.andrena70.crimeshare;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by yusufazishty on 1/5/2016.
 */
public class ExternalFonts extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //fontPath
        String fontPathUbuntu_B = "fonts/Ubuntu-B.ttf";
        String fontPathUbuntu_L = "fonts/Ubuntu-L.ttf";
        String fontPathUbuntu_LI = "fonts/Ubuntu-LI.ttf";
        String fontPathUbuntu_R = "fonts/Ubuntu-R.ttf";

        //textView Label
        TextView txtCrime = (TextView) findViewById(R.id.CrimeShareText);
        TextView txtGetNews = (TextView) findViewById(R.id.getNewsText);
        TextView txtReportNews = (TextView) findViewById(R.id.reportNewsText);
        TextView txtAboutUs = (TextView) findViewById(R.id.aboutusText);
        TextView txtRateApp = (TextView) findViewById(R.id.rateAppText);

        //Loading Font Face
        Typeface ubuntu_B = Typeface.createFromAsset(getAssets(), fontPathUbuntu_B);
        Typeface ubuntu_L = Typeface.createFromAsset(getAssets(), fontPathUbuntu_L);
        Typeface ubuntu_LI = Typeface.createFromAsset(getAssets(), fontPathUbuntu_LI);
        Typeface ubuntu_R = Typeface.createFromAsset(getAssets(), fontPathUbuntu_R);

        //Applying Font
        txtCrime.setTypeface(ubuntu_B);
        txtGetNews.setTypeface(ubuntu_L);
        txtReportNews.setTypeface(ubuntu_L);
        txtAboutUs.setTypeface(ubuntu_L);
        txtRateApp.setTypeface(ubuntu_L);



    }
}
