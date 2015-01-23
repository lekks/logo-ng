package com.ldir.logo.fieldviews;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.ldir.logo.graphics.Sprites;

import java.util.Random;

/**
 * Created by Ldir on 29.09.13.
 */

/*



 */

public class Transition {

    private int mGoal;
    private int mCurrent;
    private long mStateTime;
    private int mState;
    private Sprites mSprites;
    private Rect mRect;
    private Rect mRectTmp = new Rect();
    private int mCX, mCY;
    private int mSize, mHSize;
    private boolean mHorizontal;
    private static Random mRnd = new Random();

    final static int TRANS_STATE_FIX=0;
    final static int TRANS_STATE_GO=1;
    final static int TRANS_STATE_FADE_OUT =2;
    final static int TRANS_STATE_FADE_IN =3;

    public Transition(Rect rect, Sprites sprites) {
        this.mSprites = sprites;
        this.mRect = rect;
        mCX =rect.centerX();
        mCY =rect.centerY();
        mSize = rect.width();
        mHSize = mSize /2;
    }

    public void setGoal(int goal,long sysTime)
    {
//        Log.i("Verbose", "mState mGoal " + mGoal+"(mCurrent "+this.mGoal+"),mState="+mState);
        if(this.mGoal != goal) {
            setState(TRANS_STATE_GO, sysTime);
            mCurrent = this.mGoal;
            this.mGoal = goal;
        }
    }

    private final void setState(int state, long sysTIme)
    {
        if(this.mState != state) {
            mStateTime = sysTIme;
//            Log.i("Verbose", "mState to " + mState);

//            switch(mState){
//                case TRANS_STATE_GO:
//                    break;
//            }
            this.mState = state;
        }
    }

    public boolean transStep(Canvas canvas, long sysTime)
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
                canvas.drawBitmap(mSprites.pic[mCurrent], null, mRectTmp, null);
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
                    canvas.drawBitmap(mSprites.pic[mGoal], null, mRectTmp, null);
                } else {
                    setState(TRANS_STATE_FIX, sysTime);
                }
        }
        if (mState == TRANS_STATE_FIX) {
                if(mGoal != 0)
                    canvas.drawBitmap(mSprites.pic[mGoal], null, mRect, null);
                return true;
        }
        return false;
    }

    public void recycle(){
        mSprites.recycle();
    }
}
