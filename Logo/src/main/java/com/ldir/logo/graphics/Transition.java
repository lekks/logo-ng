package com.ldir.logo.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Ldir on 29.09.13.
 */

/*



 */

public class Transition {

    private int goal;
    private int current;

    private long stateTime;

    private int state;
    private Sprites sprites;
    private Bitmap backgr;
    private Rect rect;
    private Rect rectBuf = new Rect();
    private Paint paint;


    final static int TRANS_STATE_FIX=0;
    final static int TRANS_STATE_GO=1;
    final static int TRANS_STATE_FADE_OUT =2;
    final static int TRANS_STATE_FADE_IN =3;



    public Transition(Rect rect, Sprites sprites, Bitmap backgr, Paint paint ) {
        this.sprites = sprites;
        this.rect = rect;
        this.backgr = backgr;
    }

    public void setGoal(int goal,long sysTime)
    {
//        Log.i("Verbose", "state goal " + goal+"(current "+this.goal+"),state="+state);
        if(this.goal != goal) {
            setState(TRANS_STATE_GO, sysTime);
            current = this.goal;
            this.goal = goal;
        }
    }

    private final void setState(int state, long sysTIme)
    {
        if(this.state != state) {
            stateTime = sysTIme;
//            Log.i("Verbose", "state to " + state);

//            switch(state){
//                case TRANS_STATE_GO:
//                    break;
//            }
            this.state = state;
        }
    }

    public boolean transStep(Canvas canvas, long sysTime)
    {
        long trTime = sysTime - stateTime;
        final int TR_TIME = 500;

        canvas.drawBitmap(backgr, null, rect, paint);

        switch (state) {
            case TRANS_STATE_GO:
                if(current == 0)
                    setState(TRANS_STATE_FADE_IN, sysTime);
                else
                    setState(TRANS_STATE_FADE_OUT, sysTime);
                trTime = sysTime - stateTime;
                //no break;
            case TRANS_STATE_FADE_OUT:
                if(trTime < TR_TIME) {
                    float ph=1.0f-(float)trTime/TR_TIME;
                    int x=rect.centerX();
                    int y=rect.centerY();
                    int hs = (int)((float)rect.width()*ph/2);
                    int hh = rect.height()/2;
                    rectBuf.set(x - hs, y - hh, x + hs, y + hh);
                    canvas.drawBitmap(sprites.pic[current], null, rectBuf, paint);
                    break;
                } else {
                    if(goal == 0)
                        setState(TRANS_STATE_FIX, sysTime);
                    else
                        setState(TRANS_STATE_FADE_IN, sysTime);
                    trTime = sysTime - stateTime;
                }
                //no break;
            case TRANS_STATE_FADE_IN:
                if(trTime < TR_TIME) {
                    float ph=(float)trTime/TR_TIME;
                    int x=rect.centerX();
                    int y=rect.centerY();
                    int hs = (int)((float)rect.width()*ph/2);
                    int hh = rect.height()/2;
                    rectBuf.set(x - hs, y - hh, x + hs, y + hh);
                    canvas.drawBitmap(sprites.pic[goal], null, rectBuf, paint);
                    break;
                } else {
                    setState(TRANS_STATE_FIX, sysTime);
                }
                //no break;
            case TRANS_STATE_FIX:
                if(goal != 0)
                    canvas.drawBitmap(sprites.pic[goal], null, rect, paint);
                return true;
        }
        return false;
    }
}
