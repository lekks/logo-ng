package com.ldir.logo.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;
import android.view.SurfaceHolder;

import com.ldir.logo.game.GameMap;

/**
 * Created by Ldir on 27.09.13.
 */
public class SurfaceRender extends Thread {
    private boolean mRun;
    private SurfaceHolder surfaceHolder;
    private Object  refresh = new Object();
    private GameMap map;
    private float cSize;
    private float hsize;
    private int cols, rows;

    Cell cells[][];
    Sprites sprites;


    private class Cell {
        //	Rect rect=new Rect();
        private float x;
        private float y;

        Cell(int row, int col, float size) {
            this.x=(col+0.5f)*size;
            this.y=(row+0.5f)*size;
            //float s=span*0.42f;
            //rect.set((int)(x-s), (int)(y-s), (int)(x+s), (int)(y+s));
        }

        boolean test(float cX, float cY){
            return cX > (x- hsize) && cX < (x+ hsize) && cY > (y- hsize) && cY < (y+ hsize);
        }

        void draw(Canvas canvas, Paint paint, byte val){
            //canvas.drawRect(rect, paint);
            canvas.drawBitmap(sprites.pic[0], x- hsize, y- hsize, paint);
            if(val>0) {
                canvas.drawBitmap(sprites.pic[val], x- hsize, y- hsize, paint);
//                RectF dst = new RectF(x-hsize, y-hsize,x+hsize , y+ hsize);
//                canvas.drawBitmap(sprites.pic[val], null,dst, paint);
            }
            //canvas.drawText(String.format("%i",val), x, y, paint);
        }
    }

    public void printNumbers(Canvas canvas, Paint paint) {
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                cells[i][j].draw(canvas, paint,map.get(i,j));
            }
        }
    }

    public boolean findCell(float cX, float cY, GameMap.Pos retPos) { // TODO Оптимизировать, убрать цикл
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(cells[i][j].test(cX, cY)){
                    retPos.set(i,j);
                    return true;
                }
            }
        }
        return false;
    }


    public SurfaceRender (SurfaceHolder surfaceHolder, GameMap gameMap, float sellSize ) {
        this.surfaceHolder = surfaceHolder;
        cSize = sellSize;
        map = gameMap;
        this.rows=gameMap.ROWS;
        this.cols=gameMap.COLS;
        hsize = sellSize/2;

        sprites = new Sprites((int) FloatMath.floor(sellSize));
        cells = new Cell[rows][];
        for(int i=0;i<rows;i++){
            cells[i] = new Cell[cols];
            for(int j=0;j<cols;j++){
                cells[i][j]= new Cell(i,j,sellSize);
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
                    canvas.drawBitmap(sprites.pic[0],0,0,paint);

                    for(int i=0;i<rows;i++){
                        for(int j=0;j<cols;j++){
                            cells[i][j].draw(canvas, paint,map.get(i,j));
                        }
                    }


//                    if(i>0)
//                        canvas.drawBitmap(sprites.pic[i],0,0,paint);
//                    if(++i>=sprites.pic.length)
//                        i=0;

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
