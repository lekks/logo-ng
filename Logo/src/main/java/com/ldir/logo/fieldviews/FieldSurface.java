package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.SurfaceRender;

/**
 * Created by Ldir on 27.09.13.
 */
public class FieldSurface extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceRender drawThread;

    protected int sizeX=1;
    protected int sizeY=1;
    float fspan=0; // Размер клетки точек
    int span=0;

    protected void init(){
        getHolder().addCallback(this);
    }

    public FieldSurface(Context context) {
        super(context);
        init();
    }

    public FieldSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
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


    //SurfaceHolder.Callback
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        sizeX = width;
        sizeY = height;
        this.fspan = Math.min((float)sizeX/(float)GameMap.COLS, (float)sizeY/(float)GameMap.ROWS);
        this.span = Math.round(fspan);
        Log.i("Verbose", "Field surface size changed from " + oldw + "," + oldh + " to " + width + "," + height + " ;span " + span + "," + "(" + fspan + ")");
    }


//    class Render extends Thread

    //SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("Verbose", "surfaceCreated");
        drawThread = new SurfaceRender(getHolder());
        drawThread.start();

    }

    //SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i("Verbose", "surfaceChanged " + format + "," + width + "," + height);
    }

    //SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("Verbose", "surfaceDestroyed");
        boolean retry = true;
        // завершаем работу потока
        drawThread.close();
        drawThread = null;
    }

    public void repaintView()
    {
        if(drawThread!=null)
            drawThread.repaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Log.i("Verbose", "surface onDraw");
        canvas.drawColor(Color.GREEN);

    }

}
