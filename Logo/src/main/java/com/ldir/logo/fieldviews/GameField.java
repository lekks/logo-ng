package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.FieldRender;


public class GameField extends FieldView {
	private Paint paint = new Paint();
    private FieldRender render;
    private GameMap.Pos clickPos = new GameMap.Pos(); // Чтоб каждый раз не создавать
    private FieldPressHandler fieldPressHandler;

    public interface FieldPressHandler {
        void onPress(GameMap.Pos retPos);
    }

    public void setFieldPressHandler(FieldPressHandler handler){
        fieldPressHandler = handler;
    }

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
                if(render.findCell(event.getX(), event.getY(), clickPos)){
                    if(fieldPressHandler != null)
                        fieldPressHandler.onPress(clickPos);
                }
                break;
        }
        return true;
    }

	public void drawField() {
		render.printNumbers(frameCanvas, paint);
		invalidate();
	}
    protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(framebuf, 0, 0, paint);
//		if(Game.win) canvas.drawText(String.format("WIN"), 2, 2+paint.getTextSize(), paint);
	}
	
}


