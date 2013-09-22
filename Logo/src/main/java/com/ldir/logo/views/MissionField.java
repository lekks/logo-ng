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
	private Paint borderPaint = new Paint();
	private Paint planePaint = new Paint();
	private Paint fieldPaint = new Paint();
    FieldRender render;

	private void init() {
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(1);
		paint.setAlpha(255);
		borderPaint.setStyle(Style.STROKE);// Зачем это?
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStrokeWidth(1);
		fieldPaint.setColor(Color.BLUE);
		fieldPaint.setStrokeWidth(3);
		planePaint.setColor(Color.BLACK);
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
		
		canvas.drawColor(Color.WHITE);
		paint.setTextSize(10);
//		canvas.drawText(String.format("Hello "), 2, 2+paint.getTextSize(), paint);
//		drawGrid(canvas,fieldPaint);
		render.printNumbers(canvas, borderPaint);
		//invalidate();
	}
	
}


