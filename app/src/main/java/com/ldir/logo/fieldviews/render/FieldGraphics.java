package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;

import com.ldir.logo.R;

/**
 * Created by Ldir on 25.01.2015.
 */
public class FieldGraphics {
    public static Bitmap makeUnderlayer(int size){
        return Sprite.get(R.drawable.grid, size);
    }

    public static Bitmap[] makeStrites(int size, Bitmap[] sprites){
        if (sprites == null)
            sprites= new Bitmap[5];
        sprites[1]= Sprite.get(R.drawable.s1,size);
        sprites[2]= Sprite.get(R.drawable.s2,size);
        sprites[3]= Sprite.get(R.drawable.s3,size);
        sprites[4]= Sprite.get(R.drawable.s4,size);
        return sprites;
    }
}
