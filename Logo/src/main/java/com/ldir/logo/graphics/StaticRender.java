package com.ldir.logo.graphics;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ldir.logo.game.GameMap;

public class StaticRender {
	private Rect cells[][];
    private GameMap map;
    private Sprites sprites;
    private int cols, rows;

	public void paint(Canvas canvas, Paint paint) {
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                int val = map.get(i,j);
                Rect rect = cells[i][j];
                canvas.drawBitmap(sprites.pic[0], rect.left, rect.top, paint);
                //canvas.drawBitmap(sprites.pic[0], null,rect, paint);
                if(val>0) {
                    canvas.drawBitmap(sprites.pic[val], rect.left, rect.top, paint);
                    //canvas.drawBitmap(sprites.pic[val], null,rect, paint);
                }
            }
        }
	}

    public StaticRender(float span, GameMap gameMap) {
        if(gameMap==null)
            gameMap=new GameMap(); // Для дизаянера интерфейса

        this.map=gameMap;
        this.rows=gameMap.ROWS;
        this.cols=gameMap.COLS;

        int size = (int)span;
        sprites = new Sprites((int) size);
        cells = new Rect[rows][];
        for(int i=0;i<rows;i++){
            cells[i] = new Rect[cols];
            for(int j=0;j<cols;j++){
                cells[i][j]= new Rect(j*size, i*size,(j+1)*size, (i+1)*size);
            }
        }
    }


}

