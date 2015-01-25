package com.ldir.logo.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseIntArray;

import com.ldir.logo.R;
import com.ldir.logo.platform.GameApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ldir on 24.01.2015.
 */
public class Sprite {
    private static HashMap<Integer, HashMap<Integer,Bitmap>> cache = new HashMap<Integer,HashMap<Integer,Bitmap>>(); //TODO SparseArray
//    private static SparseIntArray<Integer, SparseIntArray<Bitmap>> s;//cache = new HashMap<Integer,HashMap<Integer,Bitmap>>();


    private static Bitmap makeBitmap(int id,int size) // can throw NullPointerException
    {
        Bitmap orig = BitmapFactory.decodeResource(GameApp.getAppResources(), id);
        Bitmap res = Bitmap.createScaledBitmap(orig, size,size, true);
        orig.recycle();
        return res;
    }

    public static int countCacheForTest()  {
        int cnt=0;
        for (Map.Entry<Integer, HashMap<Integer,Bitmap>> entry : cache.entrySet()) {
            Log.i("Sprite", "id:" + entry.getKey());
            HashMap<Integer, Bitmap> sizes = entry.getValue();
            for (Map.Entry<Integer, Bitmap> pic : sizes.entrySet()) {
                Log.i("Sprite", "pic size:" + pic.getKey());
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
        HashMap<Integer,Bitmap> sizes = cache.get(id);
        if (sizes == null) { // we have id
            sizes = new HashMap<>();
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
