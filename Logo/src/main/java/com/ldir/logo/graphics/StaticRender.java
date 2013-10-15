package com.ldir.logo.graphics;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ldir.logo.game.GameMap;

public class StaticRender {
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
        Log.i("Verbose", "Static render init " + width + "," + height + " ;span " + "," + "(" + fspan + ")");

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


}

