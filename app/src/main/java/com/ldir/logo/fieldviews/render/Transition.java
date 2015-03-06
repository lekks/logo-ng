package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Ldir on 29.09.13.
 */

/*



 */

class Transition {

    private int mGoal;
    private int mCurrent;
    private long mStateTime;
    private int mState;
    private Bitmap[] mSprites;
    private Rect mRect;
    private Rect mRectTmp = new Rect();
    private int mCX, mCY;
    private int mHSize;
    private boolean mHorizontal;
    private static Random mRnd = new Random();

    private final static int TRANS_STATE_FIX=0;
    private final static int TRANS_STATE_GO=1;
    private final static int TRANS_STATE_FADE_OUT =2;
    private final static int TRANS_STATE_FADE_IN =3;

    public Transition(Rect rect, Bitmap[] sprites) {
        this.mSprites = sprites;
        this.mRect = rect;
        mCX =rect.centerX();
        mCY =rect.centerY();
        mHSize = rect.width() /2;
    }

    public final void setGoal(int goal,long sysTime)
    {
        if(this.mGoal != goal) {
            setState(TRANS_STATE_GO, sysTime);
            mCurrent = this.mGoal;
            this.mGoal = goal;
        }
    }

    private void setState(int state, long sysTIme)
    {
        if(this.mState != state) {
            mStateTime = sysTIme;
            this.mState = state;
        }
    }

    public final boolean transStep(Canvas canvas, long sysTime)
    {
        long trTime = sysTime - mStateTime;
        final int TR_TIME = 250;

//        canvas.drawBitmap(backgr, null, mRect, paint);

        if (mState == TRANS_STATE_GO) {
            mHorizontal = mRnd.nextBoolean();
            if(mCurrent == 0)
                setState(TRANS_STATE_FADE_IN, sysTime);
            else
                setState(TRANS_STATE_FADE_OUT, sysTime);
            trTime = sysTime - mStateTime;
        }

        if (mState == TRANS_STATE_FADE_OUT) {
            if(trTime < TR_TIME) {
                float ph=1.0f-(float)trTime/TR_TIME;
                int hs = (int)((float) mHSize *ph);
                if(mHorizontal)
                    mRectTmp.set(mCX - hs, mCY - mHSize, mCX + hs, mCY + mHSize);
                else
                    mRectTmp.set(mCX - mHSize, mCY - hs, mCX + mHSize, mCY + hs);
                canvas.drawBitmap(mSprites[mCurrent], null, mRectTmp, null);
            } else {
                if(mGoal == 0)
                    setState(TRANS_STATE_FIX, sysTime);
                else
                    setState(TRANS_STATE_FADE_IN, sysTime);
                trTime = sysTime - mStateTime;
            }
        }
        if (mState == TRANS_STATE_FADE_IN) {
                if(trTime < TR_TIME) {
                    float ph=(float)trTime/TR_TIME;
                    int hs = (int)((float) mHSize *ph);
                    if(mHorizontal)
                        mRectTmp.set(mCX - hs, mCY - mHSize, mCX + hs, mCY + mHSize);
                    else
                        mRectTmp.set(mCX - mHSize, mCY - hs, mCX + mHSize, mCY + hs);
                    canvas.drawBitmap(mSprites[mGoal], null, mRectTmp, null);
                } else {
                    setState(TRANS_STATE_FIX, sysTime);
                }
        }
        if (mState == TRANS_STATE_FIX) {
                if(mGoal != 0)
                    canvas.drawBitmap(mSprites[mGoal], null, mRect, null);
                return true;
        }
        return false;
    }

    public void recycle(){
    }
}
