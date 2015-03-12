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
import com.ldir.logo.game.Levels;


public class LevelField extends android.view.View {
    private final LevelRender mRender=new LevelRender();

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


    public void setLevel(GameLevel level) {
        mRender.loadLevel(level.map);
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

    public void destroy() {
    }

}

class LevelRender {
    private boolean mDisabled=true;
    private GameMap mGameMap;
    private float mCellSize;
    private final Bitmap[] mSprites=new Bitmap[5];
    private Bitmap mUnderlayer;
    private final Rect rect = new Rect();

    private static final Paint paint = new Paint();

    public void setSize(int size) {
        mCellSize = GameMap.calcCellSize(size,size);
        if(size>0) {
            FieldGraphics.makeStrites(mCellSize, mSprites);
            mUnderlayer = FieldGraphics.makeUnderlayer(size);
        }
    }

    public void loadLevel(GameMap map) {
        mGameMap = map;
    }

    public void paint(Canvas canvas) {
        if(mCellSize == 0) // бывает и такое
            return;
        canvas.drawBitmap(mUnderlayer, 0, 0, paint);
        if (mGameMap == null)
            return;
        for(int i=0;i< GameMap.ROWS;i++){
            for(int j=0;j< GameMap.COLS;j++){
                int val = mGameMap.get(i, j);
                FieldGraphics.placeRect(rect,i,j,mCellSize);
                if(val>0 && mSprites[val] != null) {
                    canvas.drawBitmap(mSprites[val], rect.left, rect.top, paint);
                }
            }
        }
    }

}
