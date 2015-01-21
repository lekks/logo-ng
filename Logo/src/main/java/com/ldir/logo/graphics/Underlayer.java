package com.ldir.logo.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

import com.ldir.logo.R;

public class Underlayer {
    public static Bitmap orig;
    public Bitmap pic;
    static Paint paint = new Paint();

    //fixme рециркулировать и обобщить со спрайтами

    public Underlayer(int size) {
        if (orig != null)
            pic = Bitmap.createScaledBitmap(Underlayer.orig, size, size, true);
        else { // для отладки
            pic = Bitmap.createBitmap(size, size, Config.ARGB_4444);
            pic.eraseColor(Color.GREEN);
        }
    }

    public void recycle() {
        pic.recycle();
        pic=null;
    }

    public static void load(Resources res) {
        orig = BitmapFactory.decodeResource(res, R.drawable.grid);
    }
}
