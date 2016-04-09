package net.yeonjukko.bodyend.libs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by yeonjukko on 16. 4. 6..
 */
public class AndroidImage2Bitmap {
    public Bitmap getImageBitmap(String img_url) {
        Bitmap mybitmap = null;
        try {
            URL url = new URL(img_url);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            mybitmap = BitmapFactory.decodeStream(inputStream);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return mybitmap;

    }
}
