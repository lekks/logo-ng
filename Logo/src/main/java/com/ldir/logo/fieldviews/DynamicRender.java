package com.ldir.logo.fieldviews;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.Sprites;
import com.ldir.logo.graphics.Underlayer;
import com.ldir.logo.util.Observed;

/**
 * Created by Ldir on 27.09.13.
 */
public class DynamicRender extends Thread {

    Observed.Event transitionEndEvent = new Observed.Event();

    volatile private boolean mRun;
    private SurfaceHolder surfaceHolder;
    private Object state_mon = new Object();
    private GameMap map;
    private float cSize;
    private int cols, rows;
    private Transition cells[][];
    private Underlayer underlayer;
    private Paint paint = new Paint();

    private boolean mTransitionStarted = false;

    public DynamicRender(SurfaceHolder surfaceHolder, GameMap gameMap, float sellSize, int size) {
        this.surfaceHolder = surfaceHolder;
        cSize = sellSize;
        map = gameMap;
        this.rows = gameMap.ROWS;
        this.cols = gameMap.COLS;

        Sprites sprites = new Sprites((int) sellSize);
        underlayer = new Underlayer(size);
        cells = new Transition[rows][];
        for (int i = 0; i < rows; i++) {
            cells[i] = new Transition[cols];
            for (int j = 0; j < cols; j++) {
                Rect rect = new Rect((int) (j * sellSize), (int) (i * sellSize), (int) ((j + 1) * sellSize), (int) ((i + 1) * sellSize));
                cells[i][j] = new Transition(rect, sprites, sprites.pic[0]);
            }
        }
    }

    float getcSize() {
        return cSize;
    }

    void repaint() {

        long systime = System.currentTimeMillis();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                cells[i][j].setGoal(map.get(i, j), systime);

        synchronized (state_mon) {
            mTransitionStarted = true;
            state_mon.notify();
        }
    }

    public void close() {
        boolean retry = true;
        synchronized (state_mon) {
            mRun = false;
            state_mon.notify();
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

        //TODO занести инициализацию всего сюда, переинициализацию при
        //смене картинки тоже сюда


        mRun = true;
        int i;
        int j;
        long systime;
        boolean transFinished;
        Canvas canvas;
        while (mRun) {
            canvas = null;
            try {
                transFinished = true;
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {

                    //Восстановление прозрачного фона
                    canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

                    canvas.drawBitmap(underlayer.pic, 0, 0, paint);
                    systime = System.currentTimeMillis();
                    for (i = 0; i < rows; i++) {
                        for (j = 0; j < cols; j++) {
                            if (!cells[i][j].transStep(canvas, systime))
                                transFinished = false;
                        }
                    }
                }
                synchronized (state_mon) {
                    if(transFinished) {
                        if (mTransitionStarted) {
                            mTransitionStarted = false;
                            transitionEndEvent.update();
                        }
                    }
                }


            } finally {
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }


            try {
                if(!mRun)
                    sleep(5);
                synchronized (state_mon) {
                    while ( !mTransitionStarted && mRun) {
                        state_mon.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                mRun = false;
            }
        }
    }


}
