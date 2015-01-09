package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.Sprites;
import com.ldir.logo.graphics.Underlayer;
import com.ldir.logo.util.Observed;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ldir on 27.09.13.
 */
public class GameFieldView extends android.view.View {

    private Render render;
    private Timer mTimer;
    private boolean mAnimate = false;

    protected int sizeX=1;
    protected int sizeY=1;
    float fspan=0; // Размер клетки точек

    private GameMap.Pos clickPos = new GameMap.Pos(); // Чтоб каждый раз не создавать
    private FieldPressHandler fieldPressHandler;

    private boolean mTransitionStarted = false;
    public static Observed.Event transitionEndEvent = new  Observed.Event();

    public boolean findCell(float cX, float cY, GameMap.Pos retPos) {
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

    public GameFieldView(Context context) {
        super(context);
        init();
    }
    public GameFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GameFieldView(Context context, AttributeSet attrs, int defStyle) {
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
        render = new Render(Game.gameMap,fspan, sizeX, sizeY);
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
        mTransitionStarted = true;
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
        if(render.run(canvas)) {
            mAnimate = false;
            if(mTransitionStarted) {
                mTransitionStarted = false;
                transitionEndEvent.update();
            }
        } else {
            mAnimate = true;
        }
    }

    class Render  {
        private GameMap map;
        private float cSize;
        private int cols, rows;
        private Transition cells[][];
        private Underlayer underlayer;
        private Paint paint = new Paint();

        public Render(GameMap gameMap, float sellSize,int width, int height ) {
            cSize = sellSize;
            map = gameMap;
            this.rows=gameMap.ROWS;
            this.cols=gameMap.COLS;

            Sprites sprites = new Sprites((int)sellSize);
            underlayer = new Underlayer(width);
            cells = new Transition[rows][];
            for(int i=0;i<rows;i++){
                cells[i] = new Transition[cols];
                for(int j=0;j<cols;j++){
                    Rect rect = new Rect((int)(j*sellSize), (int)(i*sellSize),(int)((j+1)*sellSize), (int)((i+1)*sellSize));
                    cells[i][j]= new Transition(rect, sprites,sprites.pic[0]);
                }
            }
        }

        public float getcSize(){
            return cSize;
        }

        public void repaint() {
            long systime = System.currentTimeMillis();
            for(int i=0;i<rows;i++)
                for(int j=0;j<cols;j++)
                    cells[i][j].setGoal(map.get(i,j),systime);
        }

        public boolean run(Canvas canvas) {
            int i;
            int j;
            boolean transFinished = true;
//        canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(underlayer.pic, 0, 0, paint);

            long systime = System.currentTimeMillis();
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    if (!cells[i][j].transStep(canvas, systime))
                        transFinished = false;
                }
            }
            return transFinished;
        }
    }
}


