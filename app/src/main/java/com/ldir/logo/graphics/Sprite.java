package com.ldir.logo.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import com.ldir.logo.platform.GameApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ldir on 24.01.2015.
 */
public class Sprite {
    private static SparseArray<SparseArray<Bitmap>> cache = new SparseArray<SparseArray<Bitmap>>(); //TODO SparseArray

    private static Bitmap makeBitmap(int id,int size) // can throw NullPointerException
    {
        Bitmap orig = BitmapFactory.decodeResource(GameApp.getAppResources(), id);
        Bitmap res = Bitmap.createScaledBitmap(orig, size,size, true);
        orig.recycle();
        return res;
    }

    public static int countCacheForTest()  {
        int cnt=0;
        int id;
        for(int i = 0; i < cache.size(); i++) {
            id = cache.keyAt(i);
            Log.i("Sprite", "id:" +id);
            SparseArray<Bitmap> sizes = cache.get(id);
            int size;
            for(int j = 0; j < sizes.size(); j++) {
                size = sizes.keyAt(j);
                Log.i("Sprite", "pic size:" + size);
                cnt++;
            }
        }
        Log.i("Sprite", "Total:" + cnt);
        return cnt;
    }

    /*
    Test if have such sprite,
        if no, creates sprite, cache it and return;
     */
    public static Bitmap get(int id, int size) {
        Bitmap pic;
        SparseArray<Bitmap> sizes = cache.get(id);
        if (sizes == null) { // we have id
            sizes = new SparseArray<>();
            cache.put(id,sizes);
            pic = null;
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
