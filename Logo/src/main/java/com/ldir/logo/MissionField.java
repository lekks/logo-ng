package com.ldir.logo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.game.GameMatrix;
import com.ldir.logo.game.MissionLoader;


public class MissionField extends FieldView {
	private Paint paint = new Paint();
	private Paint borderPaint = new Paint();
	private Paint planePaint = new Paint();
	private Paint fieldPaint = new Paint();
//	private Matrix matrix;

//	int def[][]={
//			{0,0,0,0,0,0,0,0,0,0},
//			{0,0,0,0,1,0,0,0,0,0},
//			{0,0,0,1,2,1,0,0,0,0},
//			{0,0,0,1,2,1,0,0,0,0},
//			{0,0,0,0,1,0,0,0,0,0},
//			{0,0,0,0,0,0,0,0,0,0},
//		};
//	
	private void init() {
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(1);
		paint.setAlpha(255);
		borderPaint.setStyle(Style.STROKE);
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
		Game.goalMatrix = new GameMatrix(GameMap.COLS,GameMap.ROWS,fspan);
        GameMap mission = MissionLoader.load(1);
		Game.goalMatrix.load(mission);
	}

	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.WHITE);
		paint.setTextSize(10);
//		canvas.drawText(String.format("Hello "), 2, 2+paint.getTextSize(), paint);
//		drawGrid(canvas,fieldPaint);
		Game.goalMatrix.printNumbers(canvas, borderPaint);
		//invalidate();
	}
	
}


