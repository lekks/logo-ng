package com.ldir.logo.fieldviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ldir.logo.graphics.Sprites;

import java.util.Random;

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
    private Rect rectTmp = new Rect();
    private Paint paint;
    private int cX,cY;
    private int size,hsize;
    private boolean horizontal;

    static Random rnd = new Random();

    final static int TRANS_STATE_FIX=0;
    final static int TRANS_STATE_GO=1;
    final static int TRANS_STATE_FADE_OUT =2;
    final static int TRANS_STATE_FADE_IN =3;



    public Transition(Rect rect, Sprites sprites, Bitmap backgr ) {
        this.sprites = sprites;
        this.rect = rect;
        this.backgr = backgr;
        cX=rect.centerX();
        cY=rect.centerY();
        size = rect.width();
        hsize = size/2;
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
        final int TR_TIME = 250;

//        canvas.drawBitmap(backgr, null, rect, paint);

        if (state == TRANS_STATE_GO) {
            horizontal = rnd.nextBoolean();
            if(current == 0)
                setState(TRANS_STATE_FADE_IN, sysTime);
            else
                setState(TRANS_STATE_FADE_OUT, sysTime);
            trTime = sysTime - stateTime;
        }

        if (state == TRANS_STATE_FADE_OUT) {
            if(trTime < TR_TIME) {
                float ph=1.0f-(float)trTime/TR_TIME;
                int hs = (int)((float)hsize*ph);
                if(horizontal)
                    rectTmp.set(cX - hs, cY - hsize, cX + hs, cY + hsize);
                else
                    rectTmp.set(cX - hsize, cY - hs, cX + hsize, cY + hs);
                canvas.drawBitmap(sprites.pic[current], null, rectTmp, paint);
            } else {
                if(goal == 0)
                    setState(TRANS_STATE_FIX, sysTime);
                else
                    setState(TRANS_STATE_FADE_IN, sysTime);
                trTime = sysTime - stateTime;
            }
        }
        if (state == TRANS_STATE_FADE_IN) {
                if(trTime < TR_TIME) {
                    float ph=(float)trTime/TR_TIME;
                    int hs = (int)((float)hsize*ph);
                    if(horizontal)
                        rectTmp.set(cX - hs, cY - hsize, cX + hs, cY + hsize);
                    else
                        rectTmp.set(cX - hsize, cY - hs, cX + hsize, cY + hs);
                    canvas.drawBitmap(sprites.pic[goal], null, rectTmp, paint);
                } else {
                    setState(TRANS_STATE_FIX, sysTime);
                }
        }
        if (state == TRANS_STATE_FIX) {
                if(goal != 0)
                    canvas.drawBitmap(sprites.pic[goal], null, rect, paint);
                return true;
        }
        return false;
    }
}
