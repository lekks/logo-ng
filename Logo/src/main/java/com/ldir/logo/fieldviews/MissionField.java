package com.ldir.logo.fieldviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.graphics.Sprites;
import com.ldir.logo.graphics.Underlayer;


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
        Log.v("Mission Field", "Field size changed from " + oldw + "," + oldh + " to " + width + "," + height);
        if(height>0 && width>0)
            render = new StaticRender(Game.goalMap, width, height);
        else
            render = null;

        //FIXME а рециркулировать битмапы?
    }


	protected void onDraw(Canvas canvas) {
		
//		canvas.drawColor(Color.WHITE); // предварительная заливка фона сглаживает неровности. TODO подобрать ширину спрайта
        if(render != null)
		    render.paint(canvas, paint);
	}

    public void destroy(){
        render.recycle();
    }
	
}


class StaticRender {
    private Rect cells[][];
    private GameMap map;
    private Sprites sprites;
    private Underlayer underlayer;
    private int cols, rows;

    public void paint(Canvas canvas, Paint paint) {
        canvas.drawBitmap(underlayer.pic, 0, 0, paint);
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                int val = map.get(i,j);
                Rect rect = cells[i][j];
//                canvas.drawBitmap(sprites.pic[0], rect.left, rect.top, paint);
                //canvas.drawBitmap(sprites.pic[0], null,rect, paint);
                if(val>0) {
                    canvas.drawBitmap(sprites.pic[val], rect.left, rect.top, paint);
                    //canvas.drawBitmap(sprites.pic[val], null,rect, paint);
                }
            }
        }
    }

    public StaticRender(GameMap gameMap,int width, int height) {
        if(gameMap==null)
            gameMap=new GameMap(); // Для дизаянера интерфейса

        int sizeX = width;
        int sizeY = height;
        float fspan = Math.min((float)sizeX/(float)GameMap.COLS, (float)sizeY/(float)GameMap.ROWS);
        Log.v("Mission Field", "Static render init " + width + "," + height + " ;span " + "," + "(" + fspan + ")");

        this.map=gameMap;
        this.rows=gameMap.ROWS;
        this.cols=gameMap.COLS;

        sprites = new Sprites((int) fspan);
        underlayer = new Underlayer(width);
        cells = new Rect[rows][];
        for(int i=0;i<rows;i++){
            cells[i] = new Rect[cols];
            for(int j=0;j<cols;j++){
                cells[i][j]= new Rect((int)(j*fspan), (int)(i*fspan),(int)((j+1)*fspan), (int)((i+1)*fspan));
            }
        }
    }

    public void recycle() {
        underlayer.recycle();
        sprites.recycle();
    }

}



