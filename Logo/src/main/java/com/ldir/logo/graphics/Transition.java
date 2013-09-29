package com.ldir.logo.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Ldir on 29.09.13.
 */

/*
Каждый эффект перехода - FSM,
 */

public class Transition {

    private int goal;
    private int current;

    private long startTime;

    private int step;
    private Sprites sprites;
    private Rect rect;
    private Rect rectBuf = new Rect();
    private Paint paint;

    public Transition(Rect rect, Sprites sprites, Paint paint ) {
        this.sprites = sprites;
        this.rect = rect;
    }

    public void setGoal(int goal)
    {
        this.goal = goal;
    }

    public boolean transStep(Canvas canvas, long sysTime)
    {
        if(current != goal) {
            startTime = sysTime;
            step = 1;
            current = goal;
        }

        canvas.drawBitmap(sprites.pic[0], null, rect, paint);

        if(goal>0) {
            canvas.drawBitmap(sprites.pic[goal], null, rect, paint);
            if(step == 1 && (sysTime-startTime<1000)) {
                rectBuf.set(rect);
                rectBuf.offset(10,10);
                canvas.drawBitmap(sprites.pic[goal], null, rectBuf, paint);
                return false;
            } else {
                canvas.drawBitmap(sprites.pic[goal], null, rect, paint);
                step = 0;
            }

        } else
            step = 0;
        return true;
    }
}
