package com.ldir.logo.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

import com.ldir.logo.R;

public class Underlayer {
    private static Resources mRes;
    private Bitmap pic;

    public Underlayer(int size) {
        if (mRes != null) {
            Bitmap orig = BitmapFactory.decodeResource(mRes, R.drawable.grid);
            pic = Bitmap.createScaledBitmap(orig, size, size, true);
            orig.recycle();
        } else { // для отладки
            pic = Bitmap.createBitmap(size, size, Config.ARGB_4444);
            pic.eraseColor(Color.GREEN);
        }
    }

    public void recycle() {
        pic.recycle();
        pic=null;
    }

    public Bitmap get() {
        return pic;
    }

    public static void load(Resources res) {
        mRes = res;
    }
}
