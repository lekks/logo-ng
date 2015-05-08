package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.ldir.logo.R;

/**
 * Created by Ldir on 25.01.2015.
 */
public class FieldGraphics {

    public static void placeRect(Rect rect, int x, int y, float size) {
        rect.set((int)(y*size+0.5), (int)(x*size+0.5),(int)((y+1)*size+0.5), (int)((x+1)*size+0.5));
    }


    public static Bitmap makeUnderlayer(int size){
        return Sprite.get(R.drawable.fon, size);
    }
    public static Bitmap makeLock(int size){
        return Sprite.get(R.drawable.lock, size);
    }

    public static Bitmap[] makeStrites(float size, Bitmap[] sprites){
        if (sprites == null)
            sprites= new Bitmap[5];
        int rounded = (int)(size+0.5f);
        sprites[1]= Sprite.get(R.drawable.s1,rounded);
        sprites[2]= Sprite.get(R.drawable.s2,rounded);
        sprites[3]= Sprite.get(R.drawable.s3,rounded);
        sprites[4]= Sprite.get(R.drawable.s4,rounded);
        return sprites;
    }
}
