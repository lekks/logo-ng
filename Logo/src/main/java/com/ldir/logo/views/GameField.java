package com.ldir.logo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.FieldRender;

import java.util.Stack;




public class GameField extends FieldView {
	private Paint paint = new Paint();
	private Paint borderPaint = new Paint();
	private Paint planePaint = new Paint();
	private Paint fieldPaint = new Paint();
    private FieldRender render;

    private GameMap.Pos clickPos = new GameMap.Pos();

	private Stack<GameMap> history = new Stack<GameMap>(); // TODO Переделать на Vector;

	private void construct() {
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(1);
		paint.setAlpha(255);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setColor(Color.MAGENTA);
		borderPaint.setStrokeWidth(3);
		borderPaint.setTextAlign(Align.CENTER);
		borderPaint.setTextSize(25);

		fieldPaint.setColor(Color.BLUE);
		fieldPaint.setStrokeWidth(3);
		planePaint.setColor(Color.BLACK);
	}

	// Для инициализации чере XML, в других случаях другой инициализатор
	public GameField(Context context, AttributeSet attrs) {
		super(context, attrs);
		construct();
	}
	public GameField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		construct();
	}
	public GameField(Context context) {
		super(context);
		construct();
	}

	@Override protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);
		render = new FieldRender(fspan,Game.gameMap);
		drawField();
	}

	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                GameMap last = new GameMap();
                last.assign(Game.gameMap);

                if(render.findCell(event.getX(), event.getY(), clickPos)){
                    Game.gameMap.gameMove(clickPos.row, clickPos.col);
                    if(Game.gameMap.isEqual(Game.goalMap)) {
                        Game.win = true;
                    }
                    history.add(last);
                }
                drawField();
                break;
        }
        return true;
    }

    public void reset(){
    	Game.gameMap.reset();
    	history.clear();
    	Game.win = false;
    	drawField();
    }

    public void undo(){
    	if(history.size() != 0 ){
    		GameMap last = history.pop();
            Game.gameMap.assign(last);
    		drawField();
    	}
    }
    
	private void drawField() {
		render.printNumbers(frameCanvas, borderPaint);
		invalidate();
	}
    protected void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.YELLOW);
//		canvas.drawColor(Color.WHITE);

//		drawGrid(canvas,fieldPaint);
		canvas.drawBitmap(framebuf, 0, 0, borderPaint);
		if(Game.win) canvas.drawText(String.format("WIN"), 2, 2+paint.getTextSize(), paint);
	}
	
}


