package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import com.ldir.logo.game.GameMap;

public class FieldView extends android.view.View {
	protected Bitmap framebuf;
	protected Canvas frameCanvas;

	protected int sizeX=1;
	protected int sizeY=1;
	float fspan=0; // Размер клетки точек
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
		int t = width * GameMap.ROWS / GameMap.COLS;
		if (height > t) height = t;
		t = height * GameMap.COLS / GameMap.ROWS;
		if (width > t) width = t;
		setMeasuredDimension(width, height);
	}
	
	
	
	@Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		sizeX = width;
        sizeY = height;
		this.fspan = Math.min((float)sizeX/(float)GameMap.COLS, (float)sizeY/(float)GameMap.ROWS);
		this.span = Math.round(fspan);
        Log.i("Verbose","Field size changed from "+oldw+","+oldh+" to "+width+","+height+" ;span "+span+","+"("+fspan+")");
        if(framebuf != null) framebuf.recycle();
        framebuf = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		frameCanvas = new Canvas(framebuf);
	}
	
	
//	//  Отображение простой сетки в редакторе интерфейса
//	protected void drawGrid(Canvas canvas, Paint paint) {
//		for( int i=1;i<GameMap.COLS;i++){
//			int x = i*sizeX/GameMap.COLS;
//			canvas.drawLine(x, 0, x, sizeY, paint);
//		}
//		for( int i=1;i<GameMap.ROWS;i++){
//			int y = i*sizeY/GameMap.ROWS;
//			canvas.drawLine(0, y, sizeX, y, paint);
//		}
//	};
	

}
