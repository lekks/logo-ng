package com.ldir.logo.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.ldir.logo.R;

public class Sprites {
	public static Bitmap orig[];
	public Bitmap pic[];
	static Paint paint = new Paint();
	
	//Fixme рециркулировать
	public Sprites(int size) {
		pic = new Bitmap[5];
		for(int i=0;i<pic.length;i++){
			if(orig != null)
				pic[i] = Bitmap.createScaledBitmap(Sprites.orig[i], size,size, true);
			else {
				pic[i] = Bitmap.createBitmap(size, size, Config.ARGB_4444);
				Canvas canv= new Canvas(pic[i]);
				paint.setTextAlign(Align.CENTER);
				canv.drawColor(Color.YELLOW);
				canv.drawText(String.valueOf(i), size/2, (size +paint.getTextSize())/2 , paint);
				
			}
		}
	}

	public static void load(Resources res){
		orig = new Bitmap[5];
		orig[0] =  BitmapFactory.decodeResource(res, R.drawable.s0);
		orig[1] =  BitmapFactory.decodeResource(res, R.drawable.s1);
		orig[2] =  BitmapFactory.decodeResource(res, R.drawable.s2);
		orig[3] =  BitmapFactory.decodeResource(res, R.drawable.s3);
		orig[4] =  BitmapFactory.decodeResource(res, R.drawable.s4);
	}
}
