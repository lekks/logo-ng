package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.DynamicRender;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ldir on 27.09.13.
 */
public class GameField extends android.view.View {

    private DynamicRender render;
    private Timer mTimer;
    private boolean mAnimate = false;

    protected int sizeX=1;
    protected int sizeY=1;
    float fspan=0; // Размер клетки точек

    private GameMap.Pos clickPos = new GameMap.Pos(); // Чтоб каждый раз не создавать
    private FieldPressHandler fieldPressHandler;

    public boolean findCell(float cX, float cY, GameMap.Pos retPos) { // TODO Оптимизировать, убрать цикл
        int row= (int) (cY/fspan);
        int col= (int) (cX/fspan);
        if(row < Game.gameMap.ROWS && col < GameMap.COLS) {
            retPos.set(row,col);
            return true;
        } else
            return false;
    }

    public interface FieldPressHandler {
        void onPress(GameMap.Pos retPos);
    }

    public void setFieldPressHandler(FieldPressHandler handler){
        fieldPressHandler = handler;
    }

    protected void init(){
    }

    public GameField(Context context) {
        super(context);
        init();
    }
    public GameField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GameField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int[] constraints = { getMeasuredWidth(),getMeasuredHeight()};
        GameMap.fitWidhHeight(constraints);
        setMeasuredDimension(constraints[0], constraints[1]);
    }


    //SurfaceHolder.Callback
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        sizeX = width;
        sizeY = height;
        this.fspan = Math.min((float)sizeX/(float)GameMap.COLS, (float)sizeY/(float)GameMap.ROWS);
        Log.i("Verbose", "Field surface size changed from " + oldw + "," + oldh + " to " + width + "," + height + " ;span " + "," + "(" + fspan + ")");
        render = new DynamicRender(Game.gameMap,fspan, sizeX, sizeY);
        render.repaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(findCell(event.getX(), event.getY(), clickPos)){
                    if(fieldPressHandler != null)
                        fieldPressHandler.onPress(clickPos);
                }
                break;
        }
        return true;
    }

    public void drawField() {
        render.repaint();
        invalidate();
    }
    class AccTimerTask extends TimerTask {
        @Override public void run() {
            if(mAnimate)
                postInvalidate();
        };
    };

    public void setAnimationEnable(Boolean en){
        if(en){
            if(mTimer == null) {
                mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new AccTimerTask(), 20, 20);
            }
        } else {
            if(mTimer != null){
                mTimer.cancel();
                mTimer = null;
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        if(render.run(canvas))
            mAnimate = false;
        else
            mAnimate = true;
    }



}
