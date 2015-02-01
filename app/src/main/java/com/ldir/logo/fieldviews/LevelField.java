package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.ldir.logo.fieldviews.render.FieldGraphics;
import com.ldir.logo.fieldviews.render.StaticRender;
import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;


public class LevelField extends android.view.View {
	private Paint mPaint = new Paint();
    private StaticRender mRender;

	// Для инициализации чере XML, в других случаях другой инициализатор
	public LevelField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public LevelField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public LevelField(Context context) {
		super(context);
	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int[] constraints = { getMeasuredWidth(),getMeasuredHeight()};
        GameMap.fitWidhHeight(constraints);
        setMeasuredDimension(constraints[0], constraints[1]);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        Log.v("Mission Field", "Field size changed from " + oldw + "," + oldh + " to " + width + "," + height);
        if(mRender != null)
            mRender.recycle();
        if(height>0 && width>0)
            mRender = new StaticRender(Game.getGoalMap(), width, height);
        else {
            mRender = null;
        }
    }


	protected void onDraw(Canvas canvas) {
        if(mRender != null)
		    mRender.paint(canvas, mPaint);
//        Log.v("LevelField", "surface onDraw");
//        canvas.drawColor(Color.GREEN);

    }

    public void destroy(){
        mRender.recycle();
    }
	
}

class LevelRender {
    private Rect mCells[][];
    private GameMap mMap;
    private Bitmap[] mSprites;
    private Bitmap mUnderlayer;

    private int mCols, mRows;

    public void paint(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mUnderlayer, 0, 0, paint);
        for(int i=0;i< mRows;i++){
            for(int j=0;j< mCols;j++){
                int val = mMap.get(i,j);
                Rect rect = mCells[i][j];
                if(val>0) {
                    canvas.drawBitmap(mSprites[val], rect.left, rect.top, paint);
                }
            }
        }
    }

    public LevelRender(GameMap gameMap,int width, int height) {
        if(gameMap==null)
            gameMap=new GameMap(); // Для дизаянера интерфейса

        int sizeX = width;
        int sizeY = height;
        float fspan = Math.min((float)sizeX/(float)GameMap.COLS, (float)sizeY/(float)GameMap.ROWS);
        Log.v("Mission Field", "Static mRender init " + width + "," + height + " ;span " + "," + "(" + fspan + ")");

        this.mMap =gameMap;
        this.mRows =gameMap.ROWS;
        this.mCols =gameMap.COLS;

        mSprites = FieldGraphics.makeStrites((int) fspan);
        mUnderlayer = FieldGraphics.makeUnderlayer(width);
        mCells = new Rect[mRows][];
        for(int i=0;i< mRows;i++){
            mCells[i] = new Rect[mCols];
            for(int j=0;j< mCols;j++){
                mCells[i][j]= new Rect((int)(j*fspan), (int)(i*fspan),(int)((j+1)*fspan), (int)((i+1)*fspan));
            }
        }
    }

    public void recycle() {
    }

}
