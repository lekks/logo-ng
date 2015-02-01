package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.ldir.logo.fieldviews.render.StaticRender;
import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;


public class MissionField extends android.view.View {
	private Paint mPaint = new Paint();
    private StaticRender mRender;

	// Для инициализации чере XML, в других случаях другой инициализатор
	public MissionField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MissionField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public MissionField(Context context) {
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
	}

    public void destroy(){
        mRender.recycle();
    }
	
}

