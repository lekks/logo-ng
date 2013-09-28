package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.DynamicRender;

/**
 * Created by Ldir on 27.09.13.
 */
public class GameField extends SurfaceView implements SurfaceHolder.Callback{

    private DynamicRender render;

    protected int sizeX=1;
    protected int sizeY=1;
    float fspan=0; // Размер клетки точек

    private GameMap.Pos clickPos = new GameMap.Pos(); // Чтоб каждый раз не создавать
    private FieldPressHandler fieldPressHandler;

    public interface FieldPressHandler {
        void onPress(GameMap.Pos retPos);
    }

    public void setFieldPressHandler(FieldPressHandler handler){
        fieldPressHandler = handler;
    }

    protected void init(){
        getHolder().addCallback(this);
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
    }


//    class Render extends Thread

    //SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("Verbose", "surfaceCreated");
        render = new DynamicRender(getHolder(), Game.gameMap,fspan);
        render.start();

    }

    //SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i("Verbose", "surfaceChanged " + format + "," + width + "," + height);

        // FIXME Старую убить, новую создать

    }

    //SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("Verbose", "surfaceDestroyed");
        boolean retry = true;
        // завершаем работу потока
        render.close();
        render = null;
    }

    public void drawField()
    {
        if(render !=null)
            render.repaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(render.findCell(event.getX(), event.getY(), clickPos)){
                    if(fieldPressHandler != null)
                        fieldPressHandler.onPress(clickPos);
                }
                break;
        }
        return true;
    }


    // Для дизайнера инерфейса
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Log.i("Verbose", "surface onDraw");
        canvas.drawColor(Color.GREEN);

    }

}
