package com.ldir.logo.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by Ldir on 27.09.13.
 */
public class SurfaceRender extends Thread {
    private boolean mRun;
    private SurfaceHolder surfaceHolder;
    private Object  refresh = new Object();


    public SurfaceRender (SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        mRun = true;
    }

    public void repaint() {
        synchronized (refresh) {
            refresh.notify();
        }
    }

    public void close() {
        boolean retry = true;
        synchronized (refresh) {
            mRun = false;
            refresh.notify();
        }
        while (retry) {
            try {
                join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }





    @Override
    public void run() {
        Paint paint = new Paint();
        paint.setTextSize(32);
        int i=0;
        Sprites sprites = new Sprites(100);
        while (mRun) {

            Canvas canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {

//                    canvas.drawColor(Color.RED);
//                    canvas.drawText("W" + (i++), 2, 2 + paint.getTextSize(), paint);
                    canvas.drawBitmap(sprites.pic[0],0,0,paint);
                    if(i>0)
                        canvas.drawBitmap(sprites.pic[i],0,0,paint);
                    if(++i>=sprites.pic.length)
                        i=0;
                }
            } finally {
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }


            try {
                synchronized (refresh) {
                    if(mRun)
                        refresh.wait();
                    else break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
     }
}
