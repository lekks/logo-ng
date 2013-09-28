package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.StaticRender;


public class MissionField extends android.view.View {
	private Paint paint = new Paint();
    StaticRender render;

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
        int sizeX = width;
        int sizeY = height;
        float fspan = Math.min((float)sizeX/(float)GameMap.COLS, (float)sizeY/(float)GameMap.ROWS);
        Log.i("Verbose", "Field size changed from " + oldw + "," + oldh + " to " + width + "," + height + " ;span " + "," + "(" + fspan + ")");
        render = new StaticRender(fspan,Game.goalMap);
    }


	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.WHITE); // предварительная заливка фона сглаживает неровности. TODO подобрать ширину спрайта
		render.printNumbers(canvas, paint);
	}
	
}


