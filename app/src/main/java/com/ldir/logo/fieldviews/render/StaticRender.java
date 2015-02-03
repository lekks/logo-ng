package com.ldir.logo.fieldviews.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ldir.logo.game.GameMap;

public class StaticRender {
    private Rect mCells[][];
    private GameMap mMap;
    private Bitmap[] mSprites;
    private Bitmap mUnderlayer;

    private int mCols, mRows;

    public void paint(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mUnderlayer, 0, 0, paint);
        for(int i=0;i< mRows;i++){
            for(int j=0;j< mCols;j++){
                int val = mMap.get(i,j);
                Rect rect = mCells[i][j];
                if(val>0) {
                    canvas.drawBitmap(mSprites[val], rect.left, rect.top, paint);
                }
            }
        }
    }

    public StaticRender(GameMap gameMap,int width, int height) {
        if(gameMap==null)
            gameMap=new GameMap(); // Для дизаянера интерфейса

        int sizeX = width;
        int sizeY = height;
        float fspan =  GameMap.calcCellSize(sizeX,sizeY);
        Log.v("Mission Field", "Static mRender init " + width + "," + height + " ;span " + "," + "(" + fspan + ")");

        this.mMap =gameMap;
        this.mRows =gameMap.ROWS;
        this.mCols =gameMap.COLS;

        mSprites = FieldGraphics.makeStrites((int) fspan, null);
        mUnderlayer = FieldGraphics.makeUnderlayer(width);
        mCells = new Rect[mRows][];
        for(int i=0;i< mRows;i++){
            mCells[i] = new Rect[mCols];
            for(int j=0;j< mCols;j++){
                mCells[i][j]= new Rect((int)(j*fspan), (int)(i*fspan),(int)((j+1)*fspan), (int)((i+1)*fspan));
            }
        }
    }

    public void recycle() {
    }

}


