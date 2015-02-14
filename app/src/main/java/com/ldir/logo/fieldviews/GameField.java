package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ldir.logo.fieldviews.render.DynamicRender;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.util.Observed;

/**
 * Created by Ldir on 27.09.13.
 */
public class GameField extends SurfaceView implements SurfaceHolder.Callback {

    private GameMap map;
    private int mSizeX = 1;
    private int mSizeY = 1;
    private float mFSpan = 0; // Размер клетки точек
    private DynamicRender mRender;
    private GameMap.Pos mClickPos = new GameMap.Pos(); // Чтоб каждый раз не создавать
    private FieldPressHandler mFieldPressHandler;
    public Observed.Event transitionEndEvent = new Observed.Event();

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
        mFieldPressHandler = handler;
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

    public void setMap(GameMap map)
    {
        this.map = map;
    }

    public void destroy() {
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
        mSizeX = width;
        mSizeY = height;
        this.mFSpan = GameMap.calcCellSize(mSizeX,mSizeY);
        Log.v("GameField", "Field view mSize changed from " + oldw + "," + oldh + " to " + width + "," + height + " ;span " + "," + "(" + mFSpan + ")");
    }

    private void createRender() {
        if( !isInEditMode() ) {
            mRender = new DynamicRender(getHolder(), map, mFSpan, mSizeX);
            mRender.transitionEndEvent.addObserver(this.transitionEndEvent);
            mRender.start();
            mRender.repaint();
        }
    }

    /* Захват юэкграунда, если решу перерисовывать поле
    private Bitmap getBackground(){
        int[] pos=new int[2];
        getLocationOnScreen(pos);
        View v = getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        Bitmap bitmap2= Bitmap.createBitmap(bitmap,pos[0],pos[1],getWidth(),getHeight());
        bitmap.recycle();
    }*/

    @Override //SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v("GameField", "surfaceCreated");
        //getBackground();
        createRender();//TODO стартовать рендер при создании и паузить его когда не нужен
    }


    @Override //SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.v("GameField", "surfaceChanged " + format + "," + width + "," + height);

        if (mFSpan != mRender.getcSize()) {
            mRender.close();
            createRender();
        }
    }


    @Override //SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v("GameField", "surfaceDestroyed");
        boolean retry = true;
        // завершаем работу потока
        mRender.close();
        mRender = null;
    }

    public void drawField() {
        if (mRender != null) {
            mRender.repaint();
        }
    }

    public boolean findCell(float cX, float cY, GameMap.Pos retPos) {
        int row = (int) (cY / mFSpan);
        int col = (int) (cX / mFSpan);
        if (row < GameMap.ROWS && col < GameMap.COLS) {
            retPos.set(row, col);
            return true;
        } else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (findCell(event.getX(), event.getY(), mClickPos)) {
                    if (mFieldPressHandler != null)
                        mFieldPressHandler.onPress(mClickPos);
                }
                break;
        }
        return true;
    }

    // Для дизайнера инерфейса
    @Override
    protected void onDraw(Canvas canvas) {
//        Paint paint = new Paint();
        Log.v("GameField", "surface onDraw");
        canvas.drawColor(Color.GREEN);

    }

    public interface FieldPressHandler {
        void onPress(GameMap.Pos retPos);
    }

}
