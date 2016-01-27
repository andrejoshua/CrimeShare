package com.example.andrena70.crimeshare.faceprocess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by andre.na70 on 12/30/2015.
 */
public class Pixelation {

    private Bitmap bmpResult;

    private String bitmapFolderName = null;
    private String bitmapFileName = null;

    public void captureAndSave(Context _context, List<Pair<Point, Point> > rectList, Mat _imagergb) {
        Log.w("Exception size", Integer.toString(rectList.size()));

		Bitmap bmp = null;

        int width = _imagergb.cols();
        int height = _imagergb.rows();

        Mat tmp = new Mat(height, width, CvType.CV_8U, new Scalar(3));
        try {
            Imgproc.cvtColor(_imagergb, tmp, 3);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);
        } catch (CvException e) {
            Log.d("Exception converting", e.getMessage());
        }

        if (rectList.size() == 0) {
            Save saveresult = new Save("AllResult");
            saveresult.SaveImage(_context, bmp);
            this.bitmapFileName = saveresult.getCurrentNamaFile();
            this.bitmapFolderName = saveresult.getNamaFolder();
            return;
        }

        Bitmap bmp2 = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        String urnPath = null;
        String uriFileName = null;

        if (rectList.size() > 0) {
            int TLx = (int) rectList.get(0).first.x;
            int TLy = (int) rectList.get(0).first.y;
            int BRx = (int) rectList.get(0).second.x;
            int BRy = (int) rectList.get(0).second.y;

            Log.w("TLx", Integer.toString(TLx));
            Log.w("TLy", Integer.toString(TLy));
            Log.w("BRx", Integer.toString(BRx));
            Log.w("BRy", Integer.toString(BRy));

            int h_var=(BRx-TLx)/12;
            int v_var=(BRy-TLy)/12;

            int awalh = 0, awalv = 0, r = 0, g = 0, b = 0;
            int nilai_vertical , nilai_horizontal;

            while (awalv < bmp.getHeight()) {
                while (awalh < bmp.getWidth()) {
                    if (awalv<TLy || awalh<TLx || awalv>=BRy ||awalh>=BRx)
                    {
                        if (awalv+v_var<=bmp.getHeight())
                        {
                            nilai_vertical = awalv+v_var;
                        }
                        else
                        {
                            nilai_vertical = bmp.getHeight();
                        }

                        for (int i = awalv; i < nilai_vertical; i++) {
                            if(awalh+h_var <= bmp.getWidth())
                            {
                                nilai_horizontal = awalh+h_var;
                            }
                            else
                            {
                                nilai_horizontal = bmp.getWidth();
                            }

                            for (int j = awalh; j < nilai_horizontal; j++) {
                                //Log.w("Position i", Integer.toString(i));
                                //Log.w("Position j", Integer.toString(j));
                                int pixel = bmp.getPixel(j, i);
                                r = Color.red(pixel);
                                g = Color.green(pixel);
                                b = Color.blue(pixel);
                                bmp2.setPixel(j, i, Color.rgb(b, g, r));
                            }
                        }

                    }
                    else
                    {
                        if (awalv+v_var<=bmp.getHeight())
                        {
                            nilai_vertical = awalv+v_var;
                        }
                        else
                        {
                            nilai_vertical = bmp.getHeight();
                        }

                        for (int i = awalv; i < nilai_vertical; i++) {
                            if(awalh+h_var<=bmp.getWidth())
                            {
                                nilai_horizontal = awalh+h_var;
                            }
                            else
                            {
                                nilai_horizontal = bmp.getWidth();
                            }

                            for (int j = awalh; j < nilai_horizontal; j++) {
                                int pixel = bmp.getPixel(j, i);
                                r += Color.red(pixel);
                                g += Color.green(pixel);
                                b += Color.blue(pixel);
                            }
                        }

                        r = r / (h_var*v_var);
                        g = g / (h_var*v_var);
                        b = b / (h_var*v_var);

                        if (awalv+v_var<=bmp.getHeight())
                        {
                            nilai_vertical = awalv+v_var;
                        }
                        else
                        {
                            nilai_vertical = bmp.getHeight();
                        }

                        for (int y = awalv; y < nilai_vertical; y++) {
                            if(awalh+h_var<=bmp.getWidth())
                            {
                                nilai_horizontal = awalh+h_var;
                            }
                            else
                            {
                                nilai_horizontal = bmp.getWidth();
                            }

                            for (int x = awalh; x < nilai_horizontal; x++) {
                                bmp2.setPixel(x, y, Color.rgb(b, g, r));
                            }
                        }
                    }
                    awalh += h_var;
                }
                awalv += v_var;
                awalh = 0;
            }

            bmp = bmp2.copy(bmp2.getConfig(), true);
        }

        if (rectList.size() > 1) {
            for(int p = 1; p < rectList.size(); p++){
                int TLx = (int)rectList.get(p).first.x;
                int TLy = (int)rectList.get(p).first.y;
                int BRx = (int)rectList.get(p).second.x;
                int BRy = (int)rectList.get(p).second.y;

                Log.w("TLx", Integer.toString(TLx));
                Log.w("TLy", Integer.toString(TLy));
                Log.w("BRx", Integer.toString(BRx));
                Log.w("BRy", Integer.toString(BRy));

                int h_var=(BRx-TLx)/12;
                int v_var=(BRy-TLy)/12;

                int awalh = 0, awalv = 0, r = 0, g = 0, b = 0;
                int nilai_vertical , nilai_horizontal;

                while (awalv < bmp.getHeight()) {
                    while (awalh < bmp.getWidth()) {
                        if (awalv<TLy || awalh<TLx || awalv>=BRy ||awalh>=BRx)
                        {
                            if (awalv+v_var<=bmp.getHeight())
                            {
                                nilai_vertical = awalv+v_var;
                            }
                            else
                            {
                                nilai_vertical = bmp.getHeight();
                            }

                            for (int i = awalv; i < nilai_vertical; i++) {
                                if(awalh+h_var <= bmp.getWidth())
                                {
                                    nilai_horizontal = awalh+h_var;
                                }
                                else
                                {
                                    nilai_horizontal = bmp.getWidth();
                                }

                                for (int j = awalh; j < nilai_horizontal; j++) {
                                    //Log.w("Position i", Integer.toString(i));
                                    //Log.w("Position j", Integer.toString(j));
                                    int pixel = bmp.getPixel(j, i);
                                    r = Color.red(pixel);
                                    g = Color.green(pixel);
                                    b = Color.blue(pixel);
                                    bmp2.setPixel(j, i, Color.rgb(r, g, b));
                                }
                            }

                        }
                        else
                        {
                            if (awalv+v_var<=bmp.getHeight())
                            {
                                nilai_vertical = awalv+v_var;
                            }
                            else
                            {
                                nilai_vertical = bmp.getHeight();
                            }

                            for (int i = awalv; i < nilai_vertical; i++) {
                                if(awalh+h_var<=bmp.getWidth())
                                {
                                    nilai_horizontal = awalh+h_var;
                                }
                                else
                                {
                                    nilai_horizontal = bmp.getWidth();
                                }

                                for (int j = awalh; j < nilai_horizontal; j++) {
                                    int pixel = bmp.getPixel(j, i);
                                    r += Color.red(pixel);
                                    g += Color.green(pixel);
                                    b += Color.blue(pixel);
                                }
                            }

                            r = r / (h_var*v_var);
                            g = g / (h_var*v_var);
                            b = b / (h_var*v_var);

                            if (awalv+v_var<=bmp.getHeight())
                            {
                                nilai_vertical = awalv+v_var;
                            }
                            else
                            {
                                nilai_vertical = bmp.getHeight();
                            }

                            for (int y = awalv; y < nilai_vertical; y++) {
                                if(awalh+h_var<=bmp.getWidth())
                                {
                                    nilai_horizontal = awalh+h_var;
                                }
                                else
                                {
                                    nilai_horizontal = bmp.getWidth();
                                }

                                for (int x = awalh; x < nilai_horizontal; x++) {
                                    bmp2.setPixel(x, y, Color.rgb(r, g, b));
                                }
                            }
                        }
                        awalh += h_var;
                    }
                    awalv += v_var;
                    awalh = 0;
                }

                bmp = bmp2.copy(bmp2.getConfig(), true);
            }
        }

        Save saveresult = new Save("AllResult");
        saveresult.SaveImage(_context, bmp2);
        this.bitmapFileName = saveresult.getCurrentNamaFile();
        this.bitmapFolderName = saveresult.getNamaFolder();

        this.bmpResult = bmp2.copy(bmp2.getConfig(), true);
    }

    public Bitmap getBitmap() {
        return this.bmpResult;
    }

    public String getBitmapFolderName() {
        return bitmapFolderName;
    }

    public String getBitmapFileName() {
        return bitmapFileName;
    }

    private class Save {
        private Context TheThis;
        private String NamaFolder = "/ProjectResult";
        private String NamaFile = "Result";
        private String CurrentNamaFile = null;

        public Save(String _namafile) {this.NamaFile = _namafile;}

        public void SaveImage(Context context, Bitmap bmp2)
        {
            TheThis = context;
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+NamaFolder;
            File int_dir = new File(context.getFilesDir(), NamaFile);
            String CurrentDateAndTime = getCurrentDateAndTime();
            File dir = new File(file_path);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            this.CurrentNamaFile = NamaFile + CurrentDateAndTime + ".jpg";
            File file = new File(dir, this.CurrentNamaFile);

            try
            {
                FileOutputStream fOut = new FileOutputStream(file);
                bmp2.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                MakeSureFileWasCreatedThenMakeAvailable(file);
                AbleToSave();
            }
            catch (FileNotFoundException e) {UnableToSave();}
            catch (IOException e){UnableToSave();}

        }

        private void MakeSureFileWasCreatedThenMakeAvailable(File file)
        {
            MediaScannerConnection.scanFile(TheThis, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned" + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        }

        private String getCurrentDateAndTime()
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String formattedDate = df.format(c.getTime());
            return formattedDate;
        }

        private void UnableToSave()
        {
            Toast.makeText(TheThis, "Picture cannot to gallery", Toast.LENGTH_SHORT).show();
        }

        private void AbleToSave()
        {
            Toast.makeText(TheThis, "Picture Saved be saved", Toast.LENGTH_SHORT).show();
        }

        public String getNamaFolder() {
            return NamaFolder;
        }

        public String getCurrentNamaFile() {
            return CurrentNamaFile;
        }
    }
}
