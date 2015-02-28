package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.ldir.logo.game.GameMap;
import com.ldir.logo.util.Observed;

/**
 * Created by Ldir on 27.09.13.
 */
public class DynamicRender extends Thread {
    public Observed.Event transitionEndEvent = new Observed.Event();

    private volatile boolean mRun;
    private final SurfaceHolder mSurfaceHolder;
    private Object mState_mon = new Object();
//    private GameMap mMap;
    private final float mCSize;
    private final int mCols, mRows;
    private final Transition mCells[][];
    private final Bitmap mUnderlayer;
    private final Paint mPaint = new Paint();

    private boolean mTransitionStarted = false;

    private final long curTime() {
        long t = System.currentTimeMillis();
//        Log.d("Render","Time "+t);
        return t;
    }


    public DynamicRender(SurfaceHolder surfaceHolder, float cellSize, int size) {
        this.mSurfaceHolder = surfaceHolder;
        mCSize = cellSize;
        this.mRows = GameMap.ROWS;
        this.mCols = GameMap.COLS;

        Bitmap[] sprites = FieldGraphics.makeStrites(cellSize, null);
        mUnderlayer = FieldGraphics.makeUnderlayer(size);
        mCells = new Transition[mRows][];
        for (int i = 0; i < mRows; i++) {
            mCells[i] = new Transition[mCols];
            for (int j = 0; j < mCols; j++) {
                Rect rect = new Rect();
                FieldGraphics.placeRect(rect,i,j,cellSize);
                mCells[i][j] = new Transition(rect, sprites);
            }
        }
    }

    public float getcSize() {
        return mCSize;
    }

    public void repaint(GameMap map) {
        long systime = curTime();
        synchronized (mState_mon) {
            for (int i = 0; i < mRows; i++)
                for (int j = 0; j < mCols; j++)
                    mCells[i][j].setGoal(map.get(i, j), systime);
            mTransitionStarted = true;
//            Log.d("Render","Transition start at "+systime);
            mState_mon.notify();
        }
    }

    @Override
    public final void run() {
        //TODO занести инициализацию всего сюда, переинициализацию при
        int i;
        int j;
        long updateTime = 0;
        boolean transFinished;
        boolean transitionEnded;
        Canvas canvas = null;
        mRun = true;
        while (mRun) {
            transitionEnded = false;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = mSurfaceHolder.lockCanvas();
                if(canvas != null) {
                    synchronized (mSurfaceHolder) {
                        { // Вот эта хрень 10мс отъедает!
                            //Восстановление прозрачного фона
                            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                            canvas.drawBitmap(mUnderlayer, 0, 0, mPaint);
                        }
                        updateTime = curTime();
                        synchronized (mState_mon) {
                            transFinished = true;
                            for (i = 0; i < mRows; i++) {
                                for (j = 0; j < mCols; j++) {
                                    if (!mCells[i][j].transStep(canvas, updateTime))
                                        transFinished = false;
                                }
                            }
                            if (transFinished) {
                                if (mTransitionStarted) {
                                    mTransitionStarted = false;
                                    transitionEnded = true;
                                }
                            }
                        }
                    }
                }
            } finally {
                if(canvas != null) // При закрытии окна
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            if (transitionEnded)
                transitionEndEvent.update();
            try {
                while (mRun && (curTime() < updateTime + 20)) {
                    sleep(4);
                }
                synchronized (mState_mon) {
                    while (mRun && !mTransitionStarted ) {
                        mState_mon.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                mRun = false;
            }
        }
    }

    public void close() {
        synchronized (mState_mon) {
            mRun = false;
            mState_mon.notify();
        }
        try {
            join();
        } catch (InterruptedException e) {
        }
        for (int i = 0; i < mRows; i++)
            for (int j = 0; j < mCols; j++)
                mCells[i][j].recycle();
        Log.d("Render","Closed");

    }
}
