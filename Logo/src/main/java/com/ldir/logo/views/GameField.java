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
    private FieldRender render;

    private GameMap.Pos clickPos = new GameMap.Pos();

	private Stack<GameMap> history = new Stack<GameMap>(); // TODO Переделать на Vector;

	private void construct() {
		paint.setTextSize(25);
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
		render.printNumbers(frameCanvas, paint);
		invalidate();
	}
    protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(framebuf, 0, 0, paint);
		if(Game.win) canvas.drawText(String.format("WIN"), 2, 2+paint.getTextSize(), paint);
	}
	
}


