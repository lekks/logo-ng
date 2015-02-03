package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.ldir.logo.game.GameMap;
import com.ldir.logo.util.Observed;

/**
 * Created by Ldir on 27.09.13.
 */
public class DynamicRender extends Thread {
    public Observed.Event transitionEndEvent = new Observed.Event();

    private volatile boolean mRun;
    private SurfaceHolder mSurfaceHolder;
    private Object mState_mon = new Object();
    private GameMap mMap;
    private float mCSize;
    private int mCols, mRows;
    private Transition mCells[][];
    private Bitmap mUnderlayer;
    private Paint mPaint = new Paint();

    private boolean mTransitionStarted = false;

    public DynamicRender(SurfaceHolder surfaceHolder, GameMap gameMap, float sellSize, int size) {
        this.mSurfaceHolder = surfaceHolder;
        mCSize = sellSize;
        mMap = gameMap;
        this.mRows = gameMap.ROWS;
        this.mCols = gameMap.COLS;

        Bitmap[] sprites = FieldGraphics.makeStrites((int) sellSize, null);
        mUnderlayer = FieldGraphics.makeUnderlayer(size);
        mCells = new Transition[mRows][];
        for (int i = 0; i < mRows; i++) {
            mCells[i] = new Transition[mCols];
            for (int j = 0; j < mCols; j++) {
                Rect rect = new Rect((int) (j * sellSize), (int) (i * sellSize), (int) ((j + 1) * sellSize), (int) ((i + 1) * sellSize));
                mCells[i][j] = new Transition(rect, sprites);
            }
        }
    }

    public float getcSize() {
        return mCSize;
    }

    public void repaint() {

        long systime = System.currentTimeMillis();
        for (int i = 0; i < mRows; i++)
            for (int j = 0; j < mCols; j++)
                mCells[i][j].setGoal(mMap.get(i, j), systime);

        synchronized (mState_mon) {
            mTransitionStarted = true;
            mState_mon.notify();
        }
    }

    public void close() {
        boolean retry = true;
        synchronized (mState_mon) {
            mRun = false;
            mState_mon.notify();
        }

        while (retry) {
            try {
                join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
        for (int i = 0; i < mRows; i++)
            for (int j = 0; j < mCols; j++)
                mCells[i][j].recycle();
    }

    @Override
    public void run() {

        //TODO занести инициализацию всего сюда, переинициализацию при

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
                canvas = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {

                    //Восстановление прозрачного фона
                    canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

                    canvas.drawBitmap(mUnderlayer, 0, 0, mPaint);
                    systime = System.currentTimeMillis();
                    for (i = 0; i < mRows; i++) {
                        for (j = 0; j < mCols; j++) {
                            if (!mCells[i][j].transStep(canvas, systime))
                                transFinished = false;
                        }
                    }
                }
                synchronized (mState_mon) {
                    if(transFinished) {
                        if (mTransitionStarted) {
                            mTransitionStarted = false;
                            transitionEndEvent.update();
                        }
                    }
                }
            } finally {
                if (canvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            try {
                if(!mRun)
                    sleep(5);
                synchronized (mState_mon) {
                    while ( !mTransitionStarted && mRun) {
                        mState_mon.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                mRun = false;
            }
        }
    }


}
