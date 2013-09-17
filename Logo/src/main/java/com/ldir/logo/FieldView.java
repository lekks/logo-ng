package com.ldir.logo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.ldir.logo.game.Game;

public class FieldView extends android.view.View {
	protected Bitmap framebuf;
	protected Canvas frameCanvas;

	protected int sizeX=1;
	protected int sizeY=1;
	float fspan=0;
	int span=0;
	// Для инициализации чере XML, в других случаях другой инициализатор
	public FieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public FieldView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public FieldView(Context context) {
		super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int height = getMeasuredHeight(); 
		int width = getMeasuredWidth();
		int t = width * Game.grY / Game.grX;
		if (height > t) height = t;
		t = height * Game.grX / Game.grY;
		if (width > t) width = t;
		setMeasuredDimension(width, height);
	}
	
	
	
	@Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		sizeX = width; sizeY = height;
		this.fspan = Math.min((float)sizeX/(float)Game.grX, (float)sizeY/(float)Game.grY);
		this.span = Math.round(fspan);
        Log.i("Verbose","Size changed from "+oldw+","+oldh+" to "+width+","+height+" ;span "+span+","+"("+fspan+")");
        if(framebuf != null) framebuf.recycle();
        framebuf = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		frameCanvas = new Canvas(framebuf);
	};
	
	
	//  Отображение простой сетки в редакторе интерфейса
	protected void drawGrid(Canvas canvas, Paint paint) {
		for( int i=1;i<Game.grX;i++){
			int x = i*sizeX/Game.grX;
			canvas.drawLine(x, 0, x, sizeY, paint);
		}
		for( int i=1;i<Game.grY;i++){
			int y = i*sizeY/Game.grY;
			canvas.drawLine(0, y, sizeX, y, paint);
		}
	};
	

}
