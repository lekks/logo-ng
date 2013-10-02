package com.ldir.logo.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.FloatMath;
import android.view.SurfaceHolder;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;

/**
 * Created by Ldir on 27.09.13.
 */
public class DynamicRender extends Thread {
    private boolean mRun;
    private SurfaceHolder surfaceHolder;
    private Object  refresh = new Object();
    private GameMap map;
    private float cSize;
    private int cols, rows;
    private Transition cells[][];


    public DynamicRender(SurfaceHolder surfaceHolder, GameMap gameMap, float sellSize) {
        this.surfaceHolder = surfaceHolder;
        cSize = sellSize;
        map = gameMap;
        this.rows=gameMap.ROWS;
        this.cols=gameMap.COLS;

        int size = (int)sellSize;

        Sprites sprites = new Sprites(size);
        cells = new Transition[rows][];
        for(int i=0;i<rows;i++){
            cells[i] = new Transition[cols];
            for(int j=0;j<cols;j++){
                Rect rect = new Rect(j*size, i*size,(j+1)*size, (i+1)*size);
                cells[i][j]= new Transition(rect, sprites,sprites.pic[0],  new Paint());
            }
        }
    }

    public void repaint() {
        long systime = System.currentTimeMillis();
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                cells[i][j].setGoal(map.get(i,j),systime);

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
        mRun = true;
        int i;
        int j;
        long systime;
        boolean transFinished;
        while (mRun) {

            Canvas canvas = null;
            try {
                transFinished = true;
//                synchronized (surfaceHolder) {
//                }

                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
//                synchronized (surfaceHolder) {
//                    canvas.drawColor(Color.WHITE);
                    systime = System.currentTimeMillis();
                    for(i=0;i<rows;i++){
                        for(j=0;j<cols;j++){
                            if(!cells[i][j].transStep(canvas,systime))
                                transFinished = false ;
                        }
                    }
//                }
            } finally {
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }


            try {
                if(!transFinished)
                    sleep(20);
                synchronized (refresh) {
                    if(mRun) {
                        if(transFinished)
                            refresh.wait();
                    } else
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                mRun = false; // Если поменяю цикл и забуду
                break;
            }
        }
     }
}
