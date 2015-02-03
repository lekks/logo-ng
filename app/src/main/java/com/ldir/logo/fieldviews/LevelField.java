package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.ldir.logo.fieldviews.render.FieldGraphics;
import com.ldir.logo.game.GameLevel;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.game.MissionLoader;


public class LevelField extends android.view.View {
    private LevelRender mRender=new LevelRender();

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


    public void setLevel(int level) {
        mRender.loadLevel(level);
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
        mRender.setSize(Math.min(height,width));
    }

	protected void onDraw(Canvas canvas) {
        mRender.paint(canvas);
    }
}

class LevelRender {
    private boolean mDisabled=true;
    private GameLevel mGameLevel = new GameLevel();
    private float mCellSize;
    private Bitmap[] mSprites=new Bitmap[5];
    private Bitmap mUnderlayer;
    private Rect rect = new Rect();

    private static Paint paint = new Paint();

    public void setSize(int size) {
        mCellSize = GameMap.calcCellSize(size,size);
        FieldGraphics.makeStrites((int) mCellSize, mSprites);
        mUnderlayer = FieldGraphics.makeUnderlayer(size);
    }

    public void loadLevel(int level) {
        mDisabled = ! MissionLoader.load(mGameLevel,level);
    }

    public void paint(Canvas canvas) {
        canvas.drawBitmap(mUnderlayer, 0, 0, paint);
        if (mDisabled)
            return;
        for(int i=0;i< GameMap.ROWS;i++){
            for(int j=0;j< GameMap.COLS;j++){
                int val = mGameLevel.map.get(i, j);
                rect.set((int)(j*mCellSize), (int)(i*mCellSize),(int)((j+1)*mCellSize), (int)((i+1)*mCellSize));
                if(val>0 && mSprites[val] != null) {
                    canvas.drawBitmap(mSprites[val], rect.left, rect.top, paint);
                }
            }
        }
    }

}
