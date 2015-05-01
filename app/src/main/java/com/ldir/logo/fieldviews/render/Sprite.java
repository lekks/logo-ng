package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;

import com.ldir.logo.GameApp;

/**
 * Created by Ldir on 24.01.2015.
 */

//TODO http://developer.android.com/training/displaying-bitmaps/index.html
//http://stackoverflow.com/questions/1955410/bitmapfactory-oom-driving-me-nuts/

public class Sprite {


    private final static SparseArray<SparseArray<Bitmap>> cache = new SparseArray<>();

    public static int countCacheForTest()  {
        int cnt=0;
        int id;
        for(int i = 0; i < cache.size(); i++) {
            id = cache.keyAt(i);
            Log.v("Sprite", "id:" +id);
            SparseArray<Bitmap> sizes = cache.get(id);
            int size;
            for(int j = 0; j < sizes.size(); j++) {
                size = sizes.keyAt(j);
                Log.v("Sprite", "pic size:" + size);
                cnt++;
            }
        }
        Log.v("Sprite", "Total:" + cnt);
        return cnt;
    }


/*
    private Bitmap createPreScaled(int id,int size) {
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(GameApp.getAppResources(), id, o);

        //The new size we want to scale to
        final int REQUIRED_SIZE= size;

        //Find the correct scale value. It should be the power of 2.
        int scale=1;
        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
            scale*=2;

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        Bitmap orig = BitmapFactory.decodeResource(GameApp.getAppResources(), id, o2);
        return orig;
    }
*/

    private static Bitmap makeBitmap(int id,int size) // can throw NullPointerException
    {
        Bitmap orig = BitmapFactory.decodeResource(GameApp.getAppResources(), id);
        if (orig != null) {
            Bitmap res = Bitmap.createScaledBitmap(orig, size, size, true);
            orig.recycle();
            return res;
        } else {
            Bitmap res = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
            Canvas canv= new Canvas(res);
            canv.drawColor(Color.YELLOW);
            return res;
        }
    }

    /*
    Test if have such sprite,
        if no, creates sprite, cache it and return;
     */
    public static Bitmap get(int id, int size) {
        Bitmap pic = null;
        SparseArray<Bitmap> sizes = cache.get(id);
        if (sizes == null) { // we have id
            sizes = new SparseArray<>();
            cache.put(id,sizes);
        } else {
            pic = sizes.get(size);
            if(pic != null)
                return pic;
        }
        pic = makeBitmap(id,size);
        sizes.put(size,pic);
        return pic;
    }



}
