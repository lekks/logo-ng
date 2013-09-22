package com.ldir.logo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

import com.ldir.logo.game.Game;
import com.ldir.logo.graphics.FieldRender;


public class MissionField extends FieldView {
	private Paint paint = new Paint();
    FieldRender render;

	private void init() {

	}

	// Для инициализации чере XML, в других случаях другой инициализатор
	public MissionField(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public MissionField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public MissionField(Context context) {
		super(context);
		init();
	}

	@Override protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);
		render = new FieldRender(fspan,Game.goalMap);
	}

	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.WHITE); // предварительная заливка фона сглаживает неровности. TODO подобрать ширину спрайта
		render.printNumbers(canvas, paint);
	}
	
}


