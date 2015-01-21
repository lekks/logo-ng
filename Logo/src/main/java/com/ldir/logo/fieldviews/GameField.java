package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.util.Observed;

/**
 * Created by Ldir on 27.09.13.
 */
public class GameField extends SurfaceView implements SurfaceHolder.Callback {

    protected int sizeX = 1;
    protected int sizeY = 1;
    float fspan = 0; // Размер клетки точек
    private DynamicRender render;
    private GameMap.Pos clickPos = new GameMap.Pos(); // Чтоб каждый раз не создавать
    private FieldPressHandler fieldPressHandler;
    public Observed.Event transitionEndEvent = new Observed.Event();;

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

    public void setFieldPressHandler(FieldPressHandler handler) {
        fieldPressHandler = handler;
    }

    protected void init() {
        if(! isInEditMode() ) {
            //Для поднятием над фоном
            setZOrderOnTop(true);

            //Нужно для бинарной прозрачности
            getHolder().setFormat(PixelFormat.TRANSPARENT);
            //Нужно для плавной прозрачности
//        getHolder().setFormat(PixelFormat.TRANSLUCENT);
            getHolder().addCallback(this);
        }
    }

    public void destroy() {
        Log.v("GameField", "recycle");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.v("GameField", "onMeasure " + widthMeasureSpec + "," + heightMeasureSpec);
        int[] constraints = {getMeasuredWidth(), getMeasuredHeight()};
        GameMap.fitWidhHeight(constraints);
        setMeasuredDimension(constraints[0], constraints[1]);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        sizeX = width;
        sizeY = height;
        this.fspan = Math.min((float) sizeX / (float) GameMap.COLS, (float) sizeY / (float) GameMap.ROWS);
        Log.v("GameField", "Field view size changed from " + oldw + "," + oldh + " to " + width + "," + height + " ;span " + "," + "(" + fspan + ")");
    }

    private void createRender() {
        if( !isInEditMode() ) {
            render = new DynamicRender(getHolder(), Game.gameMap, fspan, sizeX);
            render.transitionEndEvent.addObserver(this.transitionEndEvent);
            render.start();
            render.repaint();
        }
    }

//    private Bitmap getBackground(){
//        int[] pos=new int[2];
//        getLocationOnScreen(pos);
//        View v = getRootView();
//        v.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
//        v.setDrawingCacheEnabled(false);
//        Bitmap bitmap2= Bitmap.createBitmap(bitmap,pos[0],pos[1],getWidth(),getHeight());
//        bitmap.recycle();
//    }
    //SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v("GameField", "surfaceCreated");
        //getBackground();
        createRender();//TODO стартовать рендер при создании и паузить его когда не нужен
    }

    //SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.v("GameField", "surfaceChanged " + format + "," + width + "," + height);

        if (fspan != render.getcSize()) {
            render.close();
            createRender();
        }
    }

    //SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v("GameField", "surfaceDestroyed");
        boolean retry = true;
        // завершаем работу потока
        render.close();
        render = null;
    }

    public void drawField() {
        if (render != null) {
            render.repaint();
        }
    }

    public boolean findCell(float cX, float cY, GameMap.Pos retPos) { // TODO Оптимизировать, убрать цикл
        int row = (int) (cY / fspan);
        int col = (int) (cX / fspan);
        if (row < Game.gameMap.ROWS && col < GameMap.COLS) {
            retPos.set(row, col);
            return true;
        } else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (findCell(event.getX(), event.getY(), clickPos)) {
                    if (fieldPressHandler != null)
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
        Log.v("GameField", "surface onDraw");
        canvas.drawColor(Color.GREEN);

    }

    public interface FieldPressHandler {
        void onPress(GameMap.Pos retPos);
    }

}
