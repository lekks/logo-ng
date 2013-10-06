package com.ldir.logo.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.FloatMath;
import android.view.SurfaceHolder;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;

/**
 * Created by Ldir on 27.09.13.
 */
public class DynamicRender  {
    private GameMap map;
    private float cSize;
    private int cols, rows;
    private Transition cells[][];
    private Underlayer underlayer;
    private Paint paint = new Paint();

    public DynamicRender(GameMap gameMap, float sellSize,int width, int height ) {
        cSize = sellSize;
        map = gameMap;
        this.rows=gameMap.ROWS;
        this.cols=gameMap.COLS;

        Sprites sprites = new Sprites((int)sellSize);
        underlayer = new Underlayer(width);
        cells = new Transition[rows][];
        for(int i=0;i<rows;i++){
            cells[i] = new Transition[cols];
            for(int j=0;j<cols;j++){
                Rect rect = new Rect((int)(j*sellSize), (int)(i*sellSize),(int)((j+1)*sellSize), (int)((i+1)*sellSize));
                cells[i][j]= new Transition(rect, sprites,sprites.pic[0]);
            }
        }
    }

    public float getcSize(){
        return cSize;
    }

    public void repaint() {
        long systime = System.currentTimeMillis();
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                cells[i][j].setGoal(map.get(i,j),systime);
    }

    public boolean run(Canvas canvas) {
        int i;
        int j;
        boolean transFinished = true;
//        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(underlayer.pic, 0, 0, paint);

        long systime = System.currentTimeMillis();
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) {
                if (!cells[i][j].transStep(canvas, systime))
                    transFinished = false;
            }
        }
        return transFinished;
    }
}
