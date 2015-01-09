package com.ldir.logo.fieldviews.old;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ldir.logo.R;

public class FieldView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
//    private View rootView;
//
//    public void setRootView(View view) {
//        rootView =view;
//    }

    protected void init(){
        //Для поднятием над фоном
        setZOrderOnTop(true);

        //Нужно для бинарной прозрачности
        //getHolder().setFormat(PixelFormat.TRANSPARENT);

        //Нужно для плавной прозрачности
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);
    }

/*    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        Log.d("FieldView","OnDraw");
    }*/

    public FieldView(Context context) {
        super(context);
        init();
    }
    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public FieldView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        int[] pos=new int[2];
//        getLocationOnScreen(pos);
//        View v = getRootView();
//        v.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
//        v.setDrawingCacheEnabled(false);
////        getHolder().setFormat(PixelFormat.);
//
//        Bitmap bitmap2= Bitmap.createBitmap(bitmap,pos[0],pos[1],getWidth(),getHeight());
//        bitmap.recycle();

        drawThread = new DrawThread(getHolder(), getResources(),null);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        drawThread.setRunning(false);
        try {
            drawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class DrawThread extends Thread{
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private Bitmap picture;
    private Matrix matrix;
    private long prevTime;

    public DrawThread(SurfaceHolder surfaceHolder, Resources resources, Bitmap bmp){
        this.surfaceHolder = surfaceHolder;

        // загружаем картинку, которую будем отрисовывать
        picture = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        //picture = bmp;

        // формируем матрицу преобразований для картинки
        matrix = new Matrix();
        //matrix.postScale(0.4f, 0.4f);
        //matrix.postTranslate(100.0f, 100.0f);

        // сохраняем текущее время
        prevTime = System.currentTimeMillis();
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            // получаем текущее время и вычисляем разницу с предыдущим
            // сохраненным моментом времени
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            if (elapsedTime > 30){
                // если прошло больше 30 миллисекунд - сохраним текущее время
                // и повернем картинку на 2 градуса.
                // точка вращения - центр картинки
                prevTime = now;
                matrix.preRotate(2.0f, picture.getWidth() / 2, picture.getHeight() / 2);
            }
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas();
                if(canvas != null) {
                    synchronized (surfaceHolder) {
                        //canvas.drawColor(Color.BLACK);

/*
                        Paint paint = new Paint();
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawPaint(paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
*/
                        //Восстановление прозрачного фона
                        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

                        canvas.drawBitmap(picture, matrix, null);
                    }
                }
            }
            finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        picture.recycle();
    }
}