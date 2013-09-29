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


    public void printNumbers(Canvas canvas, Paint paint) {
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                cells[i][j].draw(canvas, paint,map.get(i,j));
            }
        }
    }

    public boolean findCell(float cX, float cY, GameMap.Pos retPos) { // TODO Оптимизировать, убрать цикл
        int row= (int) (cY/cSize);
        int col= (int) (cX/cSize);
        if(row < Game.gameMap.ROWS && col < GameMap.COLS) {
            retPos.set(row,col);
            return true;
        } else
            return false;
    }


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
                cells[i][j]= new Transition(rect, sprites);
            }
        }
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
        mRun = true;
        while (mRun) {

            Canvas canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {

                    canvas.drawColor(Color.WHITE);

                    for(int i=0;i<rows;i++){
                        for(int j=0;j<cols;j++){
                            cells[i][j].draw(canvas, paint,map.get(i,j));
                        }
                    }

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
